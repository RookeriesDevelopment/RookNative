package io.tryrook.rooknative.feature.healthconnect.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.rookmotion.rook.sdk.domain.enums.BackgroundReadStatus
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.rook.RookHealthConnectRepository
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.healthconnect.domain.enums.HealthConnectStatus
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectAction
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectEvent
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthConnectViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val healthConnectRepository: RookHealthConnectRepository,
    private val appPreferences: AppPreferences,
    private val launcher: Launcher,
) : ViewModel() {
    private val _events = Channel<HealthConnectEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(HealthConnectState())
    val uiState get() = _uiState.asStateFlow()

    fun onAction(action: HealthConnectAction) {
        when (action) {
            HealthConnectAction.OnResume -> initHealthConnect()

            HealthConnectAction.OnDownloadClick -> launcher.openHealthConnectOnPlayStore()

            HealthConnectAction.OnOpenSettingsClick -> healthConnectRepository.openHealthConnectSettings()

            is HealthConnectAction.OnPermissionsChanged -> {
                permissionsChanged(
                    allDataTypesPermissions = action.allDataTypesPermissions,
                    partialDataTypesPermissions = action.partialDataTypesPermissions,
                    backgroundPermissions = action.backgroundPermissions,
                )
            }

            HealthConnectAction.OnAllowAccessClick -> allowAccess()

            HealthConnectAction.OnConnectClick -> connect()
        }
    }

    private fun initHealthConnect() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(healthConnectStatus = HealthConnectStatus.LOADING) }

            val availability = healthConnectRepository.checkHealthConnectAvailability()
            val backgroundStatus = healthConnectRepository
                .checkBackgroundReadStatus()
                .getOrElse { BackgroundReadStatus.UNAVAILABLE }

            val healthConnectStatus = HealthConnectStatus.fromAvailabilityAndBackgroundStatus(
                availability = availability,
                backgroundStatus = backgroundStatus,
            )

            if (healthConnectStatus == HealthConnectStatus.READY) {
                val allPermissions = healthConnectRepository
                    .checkHealthConnectPermissions()
                    .getOrElse { false }
                val partialPermissions = healthConnectRepository
                    .checkHealthConnectPermissionsPartially()
                    .getOrElse { false }

                val dataTypesGranted = allPermissions || partialPermissions
                val backgroundGranted = backgroundStatus == BackgroundReadStatus.PERMISSION_GRANTED

                val permissionsGranted = dataTypesGranted && backgroundGranted

                _uiState.update {
                    it.copy(
                        healthConnectStatus = healthConnectStatus,
                        dataTypesGranted = dataTypesGranted,
                        backgroundGranted = backgroundGranted,
                        showAllowAccessButton = !permissionsGranted,
                    )
                }
            } else {
                _uiState.update { it.copy(healthConnectStatus = healthConnectStatus) }
            }
        }
    }

    private fun permissionsChanged(
        allDataTypesPermissions: Boolean,
        partialDataTypesPermissions: Boolean,
        backgroundPermissions: Boolean
    ) {
        viewModelScope.launch(dispatcher) {
            val dataTypesPermissions = allDataTypesPermissions || partialDataTypesPermissions
            val permissionsGranted = dataTypesPermissions && backgroundPermissions

            _uiState.update {
                it.copy(
                    dataTypesGranted = dataTypesPermissions,
                    backgroundGranted = backgroundPermissions,
                    showAllowAccessButton = !permissionsGranted,
                    showOpenSettingsButton = !allDataTypesPermissions || !backgroundPermissions,
                )
            }

            when {
                !backgroundPermissions && !dataTypesPermissions -> {
                    _events.send(HealthConnectEvent.MissingPermissions)
                }

                !backgroundPermissions -> {
                    _events.send(HealthConnectEvent.MissingBackgroundPermissions)
                }

                !dataTypesPermissions -> {
                    _events.send(HealthConnectEvent.MissingDataTypesPermissions)
                }
            }
        }
    }

    private fun allowAccess() {
        viewModelScope.launch(dispatcher) {
            val result = healthConnectRepository.requestHealthConnectPermissions().getOrElse {
                RequestPermissionsStatus.REQUEST_SENT
            }

            if (result == RequestPermissionsStatus.ALREADY_GRANTED) {
                _uiState.update {
                    it.copy(
                        dataTypesGranted = true,
                        backgroundGranted = true,
                        showAllowAccessButton = false,
                        showOpenSettingsButton = false,
                    )
                }
            } else {
                _uiState.update { it.copy(showOpenSettingsButton = true) }
            }
        }
    }

    private fun connect() {
        viewModelScope.launch(dispatcher) {
            val shouldConnect = _uiState.value.dataTypesGranted && _uiState.value.backgroundGranted

            if (shouldConnect) {
                healthConnectRepository.schedule(BuildConfig.DEBUG)
                appPreferences.toggleHealthConnect(true)
                _events.send(HealthConnectEvent.BackgroundSyncEnabled)
            } else {
                when {
                    !_uiState.value.backgroundGranted && !_uiState.value.dataTypesGranted -> {
                        _events.send(HealthConnectEvent.MissingPermissions)
                    }

                    !_uiState.value.backgroundGranted -> {
                        _events.send(HealthConnectEvent.MissingBackgroundPermissions)
                    }

                    !_uiState.value.dataTypesGranted -> {
                        _events.send(HealthConnectEvent.MissingDataTypesPermissions)
                    }
                }
            }
        }
    }
}
