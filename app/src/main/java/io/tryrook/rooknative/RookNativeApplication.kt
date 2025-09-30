package io.tryrook.rooknative

import android.app.Application
import timber.log.Timber

class RookNativeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
