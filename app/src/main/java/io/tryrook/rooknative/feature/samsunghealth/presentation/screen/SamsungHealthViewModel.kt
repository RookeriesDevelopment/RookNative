package io.tryrook.rooknative.feature.samsunghealth.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.samsunghealth.domain.enums.SamsungHealthStatus
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthAction
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthEvent
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthState
import io.tryrook.sdk.samsung.domain.enums.SHRequestPermissionsStatus
import io.tryrook.sdk.samsung.domain.enums.SamsungHealthPermission
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SamsungHealthViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val samsungHealthRepository: RookSamsungHealthRepository,
    private val appPreferences: AppPreferences,
    private val launcher: Launcher,
) : ViewModel() {
    private val _events = Channel<SamsungHealthEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(SamsungHealthState())
    val uiState get() = _uiState.asStateFlow()

    fun onAction(action: SamsungHealthAction) {
        when (action) {
            SamsungHealthAction.OnResume -> initSamsungHealth()

            SamsungHealthAction.OnDownloadClick -> launcher.openSamsungHealthOnPlayStore()

            SamsungHealthAction.OnOpenSettingsClick -> launcher.openSettings()

            SamsungHealthAction.OnOpenSamsungHealthClick -> launcher.openSamsungHealthSettings()

            is SamsungHealthAction.OnPermissionsChanged -> {
                viewModelScope.launch(dispatcher) {
                    val permissionsGranted = action.allPermissions || action.partialPermissions

                    _uiState.update {
                        it.copy(
                            permissionsGranted = permissionsGranted,
                            showAllowAccessButton = !action.allPermissions,
                        )
                    }

                    if (!permissionsGranted) {
                        _events.send(SamsungHealthEvent.MissingPermissions)
                    }
                }
            }

            SamsungHealthAction.OnAllowAccessClick -> {
                viewModelScope.launch(dispatcher) {
                    val result = samsungHealthRepository.requestSamsungHealthPermissions(
                        samsungPermissions,
                    ).getOrElse { SHRequestPermissionsStatus.REQUEST_SENT }

                    if (result == SHRequestPermissionsStatus.ALREADY_GRANTED) {
                        _uiState.update {
                            it.copy(
                                permissionsGranted = true,
                                showAllowAccessButton = false,
                            )
                        }
                    }
                }
            }

            SamsungHealthAction.OnConnectClick -> {
                viewModelScope.launch(dispatcher) {
                    if (_uiState.value.permissionsGranted) {
                        samsungHealthRepository.schedule(enableLogs = BuildConfig.DEBUG)
                        appPreferences.toggleSamsungHealth(true)
                        _events.send(SamsungHealthEvent.BackgroundSyncEnabled)
                    } else {
                        _events.send(SamsungHealthEvent.MissingPermissions)
                    }
                }
            }
        }
    }

    private fun initSamsungHealth() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(samsungHealthStatus = SamsungHealthStatus.LOADING) }

            samsungHealthRepository.checkSamsungHealthAvailability().fold(
                {
                    _uiState.update { it.copy(samsungHealthStatus = SamsungHealthStatus.ERROR) }
                },
                { samsungHealthAvailability ->
                    val samsungHealthStatus = SamsungHealthStatus.fromAvailability(
                        availability = samsungHealthAvailability,
                    )

                    if (samsungHealthStatus == SamsungHealthStatus.READY) {
                        val allPermissions = samsungHealthRepository
                            .checkSamsungHealthPermissions(samsungPermissions)
                            .getOrElse { false }
                        val partialPermissions = samsungHealthRepository
                            .checkSamsungHealthPermissionsPartially(samsungPermissions)
                            .getOrElse { false }

                        _uiState.update {
                            it.copy(
                                samsungHealthStatus = samsungHealthStatus,
                                permissionsGranted = allPermissions || partialPermissions,
                                showAllowAccessButton = !allPermissions,
                            )
                        }
                    } else {
                        _uiState.update { it.copy(samsungHealthStatus = samsungHealthStatus) }
                    }
                },
            )
        }
    }
}

private val samsungPermissions: Set<SamsungHealthPermission> = setOf(
    SamsungHealthPermission.ACTIVITY_SUMMARY,
    SamsungHealthPermission.BLOOD_GLUCOSE,
    SamsungHealthPermission.BLOOD_OXYGEN,
    SamsungHealthPermission.BLOOD_PRESSURE,
    SamsungHealthPermission.BODY_COMPOSITION,
    SamsungHealthPermission.EXERCISE,
    SamsungHealthPermission.EXERCISE_LOCATION,
    SamsungHealthPermission.FLOORS_CLIMBED,
    SamsungHealthPermission.HEART_RATE,
    SamsungHealthPermission.NUTRITION,
    SamsungHealthPermission.SLEEP,
    SamsungHealthPermission.STEPS,
    SamsungHealthPermission.WATER_INTAKE,
)
