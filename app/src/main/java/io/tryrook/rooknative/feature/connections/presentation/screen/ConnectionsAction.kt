package io.tryrook.rooknative.feature.connections.presentation.screen

import io.tryrook.rooknative.feature.connections.domain.model.Connection

sealed interface ConnectionsAction {
    data object OnResume : ConnectionsAction
    data object OnConnectionsRefresh : ConnectionsAction
    data class OnConnectClick(val connection: Connection) : ConnectionsAction
    data class OnDisconnectClick(val connection: Connection) : ConnectionsAction
}
