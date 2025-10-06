package io.tryrook.rooknative.feature.samsunghealth.domain.model

sealed interface SamsungHealthAction {
    data object OnResume : SamsungHealthAction

    data object OnDownloadClick : SamsungHealthAction

    data object OnOpenSettingsClick : SamsungHealthAction

    data object OnOpenSamsungHealthClick : SamsungHealthAction

    data class OnPermissionsChanged(
        val allPermissions: Boolean,
        val partialPermissions: Boolean,
    ) : SamsungHealthAction

    data object OnAllowAccessClick : SamsungHealthAction

    data object OnConnectClick : SamsungHealthAction
}
