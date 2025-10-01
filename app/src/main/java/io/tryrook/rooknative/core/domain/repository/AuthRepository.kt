package io.tryrook.rooknative.core.domain.repository

interface AuthRepository {
    suspend fun getUserID(): String?
    suspend fun login(userID: String)
    suspend fun logout()
}
