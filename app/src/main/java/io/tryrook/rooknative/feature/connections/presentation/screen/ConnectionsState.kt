package io.tryrook.rooknative.feature.connections.presentation.screen

import io.tryrook.rooknative.feature.connections.domain.model.Connection

data class ConnectionsState(
    val loadingApiConnections: Boolean = false,
    val apiConnections: List<Connection.Api> = emptyList(),
    val apiConnectionsError: Boolean = false,
    val loadingHealthKitConnections: Boolean = false,
    val healthKitConnections: List<Connection.HealthKit> = emptyList(),
    val healthKitConnectionsError: Boolean = false,
)
