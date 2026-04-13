package io.tryrook.rooknative.feature.login.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.raise.either
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.api.sources.domain.environment.ApiEnvironment
import io.tryrook.api.sources.domain.model.ApiConfiguration
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import io.tryrook.rooknative.core.presentation.text.UIText
import io.tryrook.rooknative.core.rook.HealthError
import io.tryrook.rooknative.core.rook.RookApiHealthRepository
import io.tryrook.rooknative.core.rook.RookHealthConnectRepository
import io.tryrook.rooknative.core.rook.RookSamsungHealthRepository
import io.tryrook.rooknative.core.rook.toUiText
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.login.domain.model.LoginAction
import io.tryrook.rooknative.feature.login.domain.model.LoginEvent
import io.tryrook.rooknative.feature.login.domain.model.LoginInputState
import io.tryrook.rooknative.feature.login.domain.model.LoginState
import io.tryrook.sdk.samsung.domain.environment.SHEnvironment
import io.tryrook.sdk.samsung.domain.model.SHConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val healthConnectRepository: RookHealthConnectRepository,
    private val samsungHealthRepository: RookSamsungHealthRepository,
    private val apiHealthRepository: RookApiHealthRepository,
) : ViewModel() {
    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow(LoginState())
    val uiState get() = _uiState.asStateFlow()

    private val _inputState = MutableStateFlow(LoginInputState())
    val inputState get() = _inputState.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUserIDInput -> {
                _inputState.update {
                    it.copy(userID = action.userID)
                }
            }

            LoginAction.OnNextClick -> {
                viewModelScope.launch(dispatcher) {
                    login()
                }
            }
        }
    }

    private suspend fun login() {
        val userID = inputState.value.userID.trim()

        if (userID.isBlank()) {
            _events.send(
                element = LoginEvent.Error(
                    message = UIText.FromResource(R.string.error_user_id),
                )
            )
        }

        _uiState.update { it.copy(loading = true) }

        initSDKs().fold(
            { healthError ->
                _uiState.update { it.copy(loading = false) }
                _events.send(LoginEvent.Error(healthError.toUiText()))
            },
            {
                initUsers(userID).fold(
                    { healthError ->
                        _uiState.update { it.copy(loading = false) }
                        _events.send(LoginEvent.Error(healthError.toUiText()))
                    },
                    {
                        authRepository.login(userID)

                        _uiState.update { it.copy(loading = false) }
                        _events.send(LoginEvent.LoggedIn)
                    },
                )
            },
        )
    }

    private suspend fun initSDKs(): Either<HealthError, Unit> {
        return either {
            val healthConnectConfiguration = RookConfiguration(
                clientUUID = BuildConfig.CLIENT_UUID,
                secret = BuildConfig.SECRET,
                environment = RookEnvironment.SANDBOX,
                packageName = BuildConfig.PACKAGE_NAME
            )

            val samsungHealthConfiguration = SHConfiguration(
                clientUUID = BuildConfig.CLIENT_UUID,
                secret = BuildConfig.SECRET,
                environment = SHEnvironment.SANDBOX,
                packageName = BuildConfig.PACKAGE_NAME
            )

            val apiConfiguration = ApiConfiguration(
                clientUUID = BuildConfig.CLIENT_UUID,
                secret = BuildConfig.SECRET,
                environment = ApiEnvironment.SANDBOX,
                packageName = BuildConfig.PACKAGE_NAME
            )

            healthConnectRepository.initRook(healthConnectConfiguration).bind()
            samsungHealthRepository.initRook(samsungHealthConfiguration).bind()
            apiHealthRepository.initRook(apiConfiguration)
        }
    }

    private suspend fun initUsers(userID: String): Either<HealthError, Unit> {
        return either {
            healthConnectRepository.updateUserID(userID).bind()
            samsungHealthRepository.updateUserID(userID).bind()
        }
    }
}
