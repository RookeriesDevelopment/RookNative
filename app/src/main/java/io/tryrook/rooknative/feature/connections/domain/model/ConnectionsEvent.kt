package io.tryrook.rooknative.feature.connections.domain.model

import io.tryrook.rooknative.core.presentation.text.UIText

sealed interface ConnectionsEvent {
    data class HealthKitConnectionRequest(val healthKitType: HealthKitType) : ConnectionsEvent
    data object AlreadyConnected : ConnectionsEvent
    data class ConnectionError(val message: UIText) : ConnectionsEvent
    data object DisconnectionNotSupported : ConnectionsEvent
    data class DisconnectionError(val message: UIText) : ConnectionsEvent
    data object Disconnected : ConnectionsEvent
}
