package io.tryrook.rooknative.core.domain.preferences

interface AppPreferences {
    fun toggleHealthConnect(enabled: Boolean)
    fun isHealthConnectEnabled(): Boolean
    fun toggleSamsungHealth(enabled: Boolean)
    fun isSamsungHealthEnabled(): Boolean
    fun clear()
}