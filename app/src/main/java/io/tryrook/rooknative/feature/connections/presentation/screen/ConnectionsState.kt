package io.tryrook.rooknative.feature.connections.presentation.screen

import io.tryrook.rooknative.feature.connections.domain.model.Connection

data class ConnectionsState(
    val connections: List<Connection> = emptyList(),
)
