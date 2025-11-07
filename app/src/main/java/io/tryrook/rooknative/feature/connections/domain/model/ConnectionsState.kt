package io.tryrook.rooknative.feature.connections.domain.model

data class ConnectionsState(
    val loadingApiConnections: Boolean = false,
    val apiConnections: List<Connection.Api> = emptyList(),
    val apiConnectionsError: Boolean = false,
    val loadingHealthKitConnections: Boolean = false,
    val healthKitConnections: List<Connection.HealthKit> = emptyList(),
    val healthKitConnectionsError: Boolean = false,
)
