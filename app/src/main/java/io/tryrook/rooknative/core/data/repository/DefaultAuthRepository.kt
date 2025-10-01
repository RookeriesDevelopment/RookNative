package io.tryrook.rooknative.core.data.repository

import android.content.Context
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class DefaultAuthRepository(context: Context) : AuthRepository {
    private val sharedPreferences by lazy {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    override suspend fun getUserID(): String? {
        delay(1.seconds) // Simulate DB delay

        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    override suspend fun login(userID: String) {
        delay(1.seconds) // Simulate Network delay

        sharedPreferences.edit().putString(USER_ID_KEY, userID).apply()
    }

    override suspend fun logout() {
        delay(1.seconds) // Simulate Network delay

        sharedPreferences.edit().remove(USER_ID_KEY).apply()
    }
}

const val USER_ID_KEY = "user_id"
