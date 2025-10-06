package io.tryrook.rooknative

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RookNativeApplication : Application() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var healthConnectRepository: RookHealthConnectRepository

    @Inject
    lateinit var samsungHealthRepository: RookSamsungHealthRepository

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (appPreferences.isHealthConnectEnabled()) {
            healthConnectRepository.schedule(BuildConfig.DEBUG)
        }

        if (appPreferences.isSamsungHealthEnabled()) {
            samsungHealthRepository.schedule(BuildConfig.DEBUG)
        }
    }
}
