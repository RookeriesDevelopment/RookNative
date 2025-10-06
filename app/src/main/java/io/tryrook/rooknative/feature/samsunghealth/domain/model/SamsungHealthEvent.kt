package io.tryrook.rooknative.feature.samsunghealth.domain.model

interface SamsungHealthEvent {
    data object BackgroundSyncEnabled : SamsungHealthEvent
    data object MissingPermissions : SamsungHealthEvent
}