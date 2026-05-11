package io.tryrook.rooknative.feature.androidsteps.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.rook.RookStepsRepository
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
                permissionsChanged(action.permissionsGranted, action.shouldRequestPermissions)
            }

            AndroidStepsAction.OnGrantPermissionsClick -> grantPermissions()

            AndroidStepsAction.OnConnectClick -> connect()
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

    private fun permissionsChanged(permissionsGranted: Boolean, shouldRequestPermissions: Boolean) {
        viewModelScope.launch(dispatcher) {
            _uiState.update {
                it.copy(
                    permissionsGranted = permissionsGranted,
                    shouldRequestPermissions = shouldRequestPermissions,
                    showGrantPermissionsButton = if (shouldRequestPermissions) {
                        !permissionsGranted
                    } else {
                        false
                    },
                    showOpenSettingsButton = !permissionsGranted && !shouldRequestPermissions,
                )
            }

            if (!permissionsGranted) {
                _events.send(AndroidStepsEvent.MissingPermissions)
            }
        }
    }

    private fun grantPermissions() {
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

    private fun connect() {
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
