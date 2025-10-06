package io.tryrook.rooknative.feature.androidsteps.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.framework.health.RookStepsRepository
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsAction
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsEvent
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidStepsViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val stepsRepository: RookStepsRepository,
    private val launcher: Launcher,
) : ViewModel() {
    private val _events = Channel<AndroidStepsEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(AndroidStepsState())
    val uiState get() = _uiState.asStateFlow()

    fun onAction(action: AndroidStepsAction) {
        when (action) {
            AndroidStepsAction.OnResume -> initAndroidSteps()

            AndroidStepsAction.OnOpenSettingsClick -> launcher.openApplicationSettings()

            is AndroidStepsAction.OnPermissionsChanged -> {
                viewModelScope.launch(dispatcher) {
                    _uiState.update {
                        it.copy(
                            permissionsGranted = action.permissionsGranted,
                            shouldRequestPermissions = action.shouldRequestPermissions,
                            showGrantPermissionsButton = if (action.shouldRequestPermissions) {
                                !action.permissionsGranted
                            } else {
                                false
                            },
                            showOpenSettingsButton = !action.permissionsGranted && !action.shouldRequestPermissions,
                        )
                    }

                    if (!action.permissionsGranted) {
                        _events.send(AndroidStepsEvent.MissingPermissions)
                    }
                }
            }

            AndroidStepsAction.OnGrantPermissionsClick -> {
                val result = stepsRepository.requestAndroidPermissions()

                if (result == RequestPermissionsStatus.ALREADY_GRANTED) {
                    _uiState.update {
                        it.copy(
                            permissionsGranted = true,
                            showGrantPermissionsButton = false,
                        )
                    }
                }
            }

            AndroidStepsAction.OnConnectClick -> {
                viewModelScope.launch(dispatcher) {
                    if (_uiState.value.permissionsGranted) {
                        stepsRepository.enableBackgroundAndroidSteps()
                        _events.send(AndroidStepsEvent.AndroidStepsEnabled)
                    } else {
                        _events.send(AndroidStepsEvent.MissingPermissions)
                    }
                }
            }
        }
    }

    private fun initAndroidSteps() {
        viewModelScope.launch(dispatcher) {
            val permissionsGranted = stepsRepository.checkAndroidPermissions()

            _uiState.update {
                it.copy(
                    permissionsGranted = permissionsGranted,
                    showGrantPermissionsButton = !permissionsGranted,
                )
            }
        }
    }
}
