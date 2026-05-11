package io.tryrook.rooknative.feature.healthconnect.domain.model

sealed interface HealthConnectAction {
    data object OnResume : HealthConnectAction

    data object OnDownloadClick : HealthConnectAction

    data object OnOpenSettingsClick : HealthConnectAction

    data class OnPermissionsChanged(
        val allDataTypesPermissions: Boolean,
        val partialDataTypesPermissions: Boolean,
        val backgroundPermissions: Boolean,
    ) : HealthConnectAction

    data object OnAllowAccessClick : HealthConnectAction

    data object OnConnectClick : HealthConnectAction
}
