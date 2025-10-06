package io.tryrook.rooknative.feature.androidsteps.domain.model

sealed interface AndroidStepsAction {
    data object OnResume : AndroidStepsAction

    data object OnOpenSettingsClick : AndroidStepsAction

    data class OnPermissionsChanged(
        val permissionsGranted: Boolean,
        val shouldRequestPermissions: Boolean,
    ) : AndroidStepsAction

    data object OnGrantPermissionsClick : AndroidStepsAction

    data object OnConnectClick : AndroidStepsAction
}
