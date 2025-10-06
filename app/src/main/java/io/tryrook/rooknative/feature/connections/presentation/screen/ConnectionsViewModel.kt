package io.tryrook.rooknative.feature.connections.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.framework.health.RookApiHealthRepository
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.core.framework.health.RookStepsRepository
import io.tryrook.rooknative.core.presentation.extension.toUiText
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.connections.domain.model.Connection
import io.tryrook.rooknative.feature.connections.domain.model.HealthKitType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    val apiHealthRepository: RookApiHealthRepository,
    val healthConnectRepository: RookHealthConnectRepository,
    val samsungHealthRepository: RookSamsungHealthRepository,
    val stepsRepository: RookStepsRepository,
    private val appPreferences: AppPreferences,
    private val launcher: Launcher,
) : ViewModel() {
    private val _events = Channel<ConnectionsEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(ConnectionsState())
    val uiState get() = _uiState.asStateFlow()

    private val connections = mutableMapOf<String, Connection>()

    init {
        viewModelScope.launch(dispatcher) {
            loadApiConnections()
        }
    }

    fun onAction(action: ConnectionsAction) {
        when (action) {
            ConnectionsAction.OnResume -> viewModelScope.launch(dispatcher) {
                loadHealthKitConnections()
            }

            ConnectionsAction.OnConnectionsRefresh -> viewModelScope.launch(dispatcher) {
                async { loadApiConnections() }
                async { loadHealthKitConnections() }
            }

            is ConnectionsAction.OnConnectClick -> viewModelScope.launch(dispatcher) {
                when (action.connection) {
                    is Connection.Api -> {
                        _uiState.update { it.copy(loadingApiConnections = true) }

                        apiHealthRepository.getDataSourceAuthorizer(
                            dataSource = action.connection.name,
                            redirectUrl = HOME_PAGE_URL,
                        ).fold(
                            { error ->
                                _uiState.update { it.copy(loadingApiConnections = false) }
                                _events.send(
                                    element = ConnectionsEvent.ConnectionError(
                                        message = error.toUiText(),
                                    )
                                )
                            },
                            { dataSourceAuthorizer ->
                                val connectionUrl = dataSourceAuthorizer.authorizationUrl

                                if (connectionUrl != null) {
                                    _uiState.update { it.copy(loadingApiConnections = false) }
                                    launcher.openUrlOnChrome(connectionUrl)
                                } else {
                                    loadApiConnections()
                                    _events.send(ConnectionsEvent.AlreadyConnected)
                                }
                            },
                        )
                    }

                    is Connection.HealthKit -> {
                        _events.send(
                            element = ConnectionsEvent.HealthKitConnectionRequest(
                                healthKitType = action.connection.healthKitType,
                            )
                        )
                    }
                }
            }

            is ConnectionsAction.OnDisconnectClick -> viewModelScope.launch(dispatcher) {
                when (action.connection) {
                    is Connection.Api -> {
                        val dataSourceType = action.connection.dataSourceType

                        if (dataSourceType == null) {
                            _events.send(ConnectionsEvent.DisconnectionNotSupported)

                            return@launch
                        }

                        _uiState.update { it.copy(loadingApiConnections = true) }

                        apiHealthRepository.revokeDataSource(dataSourceType).fold(
                            { error ->
                                _uiState.update { it.copy(loadingApiConnections = false) }
                                _events.send(
                                    element = ConnectionsEvent.DisconnectionError(
                                        message = error.toUiText(),
                                    )
                                )
                            },
                            {
                                loadApiConnections(delay = 5.seconds)
                                _events.send(ConnectionsEvent.Disconnected)
                            },
                        )
                    }

                    is Connection.HealthKit -> {
                        when (action.connection.healthKitType) {
                            HealthKitType.HEALTH_CONNECT -> {
                                healthConnectRepository.cancel()
                                appPreferences.toggleHealthConnect(false)
                                loadHealthKitConnections()
                            }

                            HealthKitType.SAMSUNG_HEALTH -> {
                                samsungHealthRepository.cancel()
                                appPreferences.toggleSamsungHealth(false)
                                loadHealthKitConnections()
                            }

                            HealthKitType.ANDROID_STEPS -> {
                                stepsRepository.disableBackgroundAndroidSteps()
                                loadHealthKitConnections()
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadApiConnections(delay: Duration = Duration.ZERO) {
        _uiState.update {
            it.copy(
                loadingApiConnections = true,
                apiConnectionsError = false,
            )
        }

        if (delay != Duration.ZERO) {
            delay(delay)
        }

        apiHealthRepository.getAuthorizedDataSourcesV2().fold(
            {
                _uiState.update { it.copy(apiConnectionsError = true) }
            },
            { dataSourcesV2 ->
                for (dataSourceV2 in dataSourcesV2) {
                    if (invalidApiDataSources.contains(dataSourceV2.name)) {
                        continue
                    }

                    connections[dataSourceV2.name] = Connection.Api(
                        name = dataSourceV2.name,
                        connected = dataSourceV2.authorized,
                        iconUrl = dataSourceV2.imageUrl,
                    )
                }
            }
        )

        _uiState.update {
            it.copy(
                loadingApiConnections = false,
                apiConnections = connections.values.filterIsInstance<Connection.Api>(),
            )
        }
    }

    private suspend fun loadHealthKitConnections() {
        _uiState.update {
            it.copy(
                loadingHealthKitConnections = true,
                healthKitConnectionsError = false,
            )
        }

        val healthConnectAvailable = healthConnectRepository.checkHealthConnectAvailability() !=
                HealthConnectAvailability.NOT_SUPPORTED
        val samsungHealthAvailable = samsungHealthRepository.isCompatible()
        val androidStepsAvailable = stepsRepository.isAvailable()

        val androidStepsEnabled = if (androidStepsAvailable) {
            stepsRepository.isBackgroundAndroidStepsActive()
        } else {
            false
        }

        val healthConnectEnabled = healthConnectRepository.isScheduled().getOrElse {
            _uiState.update { it.copy(healthKitConnectionsError = true) }
            false
        }
        val samsungHealthEnabled = samsungHealthRepository.isScheduled().getOrElse {
            _uiState.update { it.copy(healthKitConnectionsError = true) }
            false
        }

        if (healthConnectAvailable) {
            connections["Health Connect"] = Connection.HealthKit.buildHealthConnect(
                connected = healthConnectEnabled,
            )
        }

        if (samsungHealthAvailable) {
            connections["Samsung Health"] = Connection.HealthKit.buildSamsungHealth(
                connected = samsungHealthEnabled,
            )
        }

        if (androidStepsAvailable) {
            connections["Android Steps"] = Connection.HealthKit.buildAndroidSteps(
                connected = androidStepsEnabled,
            )
        }

        _uiState.update {
            it.copy(
                loadingHealthKitConnections = false,
                healthKitConnections = connections.values.filterIsInstance<Connection.HealthKit>(),
            )
        }
    }
}

private val invalidApiDataSources = setOf(
    "Health Connect",
    "Samsung Health",
    "Android",
    "Apple Health",
)

private const val HOME_PAGE_URL = "https://extraction-app-user-bind.rook-connect.com/open-app"
