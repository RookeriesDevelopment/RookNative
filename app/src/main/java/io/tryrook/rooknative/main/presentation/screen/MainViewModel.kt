package io.tryrook.rooknative.main.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.main.domain.enums.UserSessionStatus
import io.tryrook.sdk.samsung.domain.environment.SHEnvironment
import io.tryrook.sdk.samsung.domain.model.SHConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val rookHealthConnectRepository: RookHealthConnectRepository,
    private val rookSamsungHealthRepository: RookSamsungHealthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainState>(MainState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(loading = true) }

            initSDKs()
            initUsersIfNecessary()
        }
    }

    private suspend fun initSDKs() {
        val healthConnectConfiguration = RookConfiguration(
            clientUUID = BuildConfig.CLIENT_UUID,
            secretKey = BuildConfig.SECRET_KEY,
            environment = RookEnvironment.SANDBOX,
        )

        val samsungHealthConfiguration = SHConfiguration(
            clientUUID = BuildConfig.CLIENT_UUID,
            secretKey = BuildConfig.SECRET_KEY,
            environment = SHEnvironment.SANDBOX,
        )

        rookHealthConnectRepository.initRook(healthConnectConfiguration).fold(
            {
                Timber.e("Error initializing Rook SDK: $it")
            },
            {
                Timber.i("Rook SDK initialized")
            },
        )

        rookSamsungHealthRepository.initRook(samsungHealthConfiguration).fold(
            {
                Timber.e("Error initializing Rook Samsung SDK: $it")
            },
            {
                Timber.i("Rook Samsung SDK initialized")
            },
        )
    }

    private suspend fun initUsersIfNecessary() {
        val userID = authRepository.getUserID()

        if (userID == null) {
            _uiState.update {
                it.copy(
                    loading = false,
                    userSessionStatus = UserSessionStatus.NOT_LOGGED_IN,
                )
            }
            return
        }

        val healthConnectUser = rookHealthConnectRepository.getUserID()
        val samsungHealthUser = rookSamsungHealthRepository.getUserID()

        val isHealthConnectUserCorrect = healthConnectUser == userID
        val isSamsungHealthUserCorrect = samsungHealthUser == userID

        // Edge case where the ids are not the same
        if (!isHealthConnectUserCorrect) {
            rookHealthConnectRepository.updateUserID(userID).fold(
                {
                    Timber.e("Error updating healthConnectUser: $it")
                },
                {
                    Timber.i("healthConnectUser updated")
                },
            )
        }

        // Edge case where the ids are not the same
        if (!isSamsungHealthUserCorrect) {
            rookSamsungHealthRepository.updateUserID(userID).fold(
                {
                    Timber.e("Error updating samsungHealthUser: $it")
                },
                {
                    Timber.i("samsungHealthUser updated")
                },
            )
        }

        _uiState.update {
            it.copy(
                loading = false,
                userSessionStatus = UserSessionStatus.LOGGED_IN,
            )
        }
    }
}
