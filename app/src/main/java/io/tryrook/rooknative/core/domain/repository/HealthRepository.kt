package io.tryrook.rooknative.core.domain.repository

interface HealthRepository {
    suspend fun isHealthConnectEnabled(): Boolean
    suspend fun isAndroidEnabled(): Boolean
    suspend fun isApiEnabled(): Boolean
    suspend fun isSamsungHealthEnabled(): Boolean
}
