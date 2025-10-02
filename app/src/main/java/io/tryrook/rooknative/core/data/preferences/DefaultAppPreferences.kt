package io.tryrook.rooknative.core.data.preferences

import android.content.Context
import androidx.core.content.edit
import io.tryrook.rooknative.core.domain.preferences.AppPreferences

class DefaultAppPreferences(context: Context) : AppPreferences {

    private val sharedPreferences = context.getSharedPreferences(
        /* name = */ "app_preferences",
        /* mode = */ Context.MODE_PRIVATE
    )

    override fun toggleHealthConnect(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(HEALTH_CONNECT_KEY, enabled)
        }
    }

    override fun isHealthConnectEnabled(): Boolean {
        return sharedPreferences.getBoolean(HEALTH_CONNECT_KEY, false)
    }

    override fun toggleSamsungHealth(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(SAMSUNG_HEALTH_KEY, enabled) }
    }

    override fun isSamsungHealthEnabled(): Boolean {
        return sharedPreferences.getBoolean(SAMSUNG_HEALTH_KEY, false)
    }

    override fun clear() {
        val success = sharedPreferences.edit().clear().commit()

        if (!success) {
            throw IllegalStateException("Failed to clear app preferences")
        }
    }
}

private const val HEALTH_CONNECT_KEY = "background_sync"
private const val SAMSUNG_HEALTH_KEY = "samsung_health"
