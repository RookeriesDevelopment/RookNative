package io.tryrook.rooknative.feature.connections.presentation.screen

import io.tryrook.rooknative.core.presentation.text.UIText
import io.tryrook.rooknative.feature.connections.domain.model.HealthKitType

sealed interface ConnectionsEvent {
    data class HealthKitConnectionRequest(val healthKitType: HealthKitType) : ConnectionsEvent
    data object AlreadyConnected : ConnectionsEvent
    data class ConnectionError(val message: UIText) : ConnectionsEvent
    data object DisconnectionNotSupported : ConnectionsEvent
    data class DisconnectionError(val message: UIText) : ConnectionsEvent
    data object Disconnected : ConnectionsEvent
}
