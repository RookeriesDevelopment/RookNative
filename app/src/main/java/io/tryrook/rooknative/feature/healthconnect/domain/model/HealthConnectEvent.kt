package io.tryrook.rooknative.feature.healthconnect.domain.model

sealed interface HealthConnectEvent {
    data object BackgroundSyncEnabled : HealthConnectEvent
    data object MissingPermissions : HealthConnectEvent
    data object MissingBackgroundPermissions : HealthConnectEvent
    data object MissingDataTypesPermissions : HealthConnectEvent
}
