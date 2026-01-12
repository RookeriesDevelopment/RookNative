package io.tryrook.rooknative.core.domain.launcher

interface Launcher {
    fun openUrlOnChrome(url: String)
    fun openHealthConnectOnPlayStore()
    fun openSamsungHealthOnPlayStore()
    fun openSamsungHealthSettings()
    fun openSettings()
    fun openApplicationSettings()
    fun requestIgnoringBatteryOptimizations()
    fun isIgnoringBatteryOptimizations(): Boolean
}
