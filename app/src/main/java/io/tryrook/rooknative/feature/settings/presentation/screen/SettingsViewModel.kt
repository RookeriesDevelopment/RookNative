package io.tryrook.rooknative.feature.settings.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.domain.enums.DataSourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import io.tryrook.rooknative.core.framework.health.RookApiHealthRepository
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.core.framework.health.RookStepsRepository
import io.tryrook.rooknative.di.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val healthConnectRepository: RookHealthConnectRepository,
    private val samsungHealthRepository: RookSamsungHealthRepository,
    private val stepsRepository: RookStepsRepository,
    private val apiHealthRepository: RookApiHealthRepository,
    private val authRepository: AuthRepository,
    private val appPreferences: AppPreferences,
) : ViewModel() {
    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnLogoutClick -> viewModelScope.launch(dispatcher) {
                // Logout from api data sources
                //
                // You should call revoke only on data sources that are actually connected
                val jobs = disconnectableApiDataSources.map {
                    async { apiHealthRepository.revokeDataSource(it) }
                }

                jobs.awaitAll()

                // Logout from health kits
                //
                // Calling healthConnectRepository.cancel(), stepsRepository.disableBackgroundAndroidSteps() and samsungHealthRepository.cancel()
                // is enough to stop sending data (assuming you don't perform any manual sync).

                // Logout from Health Connect and Android Steps
                healthConnectRepository.cancel()
                stepsRepository.disableBackgroundAndroidSteps()

                val loggedOutFromHealthConnect = healthConnectRepository.deleteUserFromRook()
                    .isRight()

                // Logout from Samsung Health
                samsungHealthRepository.cancel()

                val loggedOutFromSamsungHealth = samsungHealthRepository.deleteUserFromRook()
                    .isRight()

                // Logout from application only if both Health Connect and Samsung Health are logged out
                //
                // You can skip the `loggedOutFromHealthConnect && loggedOutFromSamsungHealth` validation
                // is very rare for those calls to fail,
                // plus the onboarding flow in this demo will override (see updateUserID behaviour in docs) any
                // previous/remaining userID before any data/connection is send/processed,
                // however is recommended to check that the userIDs were completely removed
                if (loggedOutFromHealthConnect && loggedOutFromSamsungHealth) {
                    authRepository.logout()
                    appPreferences.clear()

                    _events.send(SettingsEvent.Logout)
                } else {
                    _events.send(SettingsEvent.LogoutError)
                }
            }
        }
    }
}

private val disconnectableApiDataSources = listOf(
    DataSourceType.OURA,
    DataSourceType.POLAR,
    DataSourceType.WHOOP,
    DataSourceType.FITBIT,
    DataSourceType.GARMIN,
    DataSourceType.WITHINGS,
)
