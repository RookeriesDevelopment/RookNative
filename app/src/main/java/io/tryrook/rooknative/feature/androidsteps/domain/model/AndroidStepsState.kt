package io.tryrook.rooknative.feature.androidsteps.domain.model

data class AndroidStepsState(
    val permissionsGranted: Boolean = false,
    val shouldRequestPermissions: Boolean = true,
    val showGrantPermissionsButton: Boolean = false,
    val showOpenSettingsButton: Boolean = false,
)
