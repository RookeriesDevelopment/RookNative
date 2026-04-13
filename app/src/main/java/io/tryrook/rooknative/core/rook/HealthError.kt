package io.tryrook.rooknative.core.rook

import io.tryrook.rooknative.core.domain.error.Error

sealed interface HealthError : Error {
    data object HttpRequestFailed : HealthError
    data object HttpRequestTimeout : HealthError
    data object Unauthorized : HealthError
    data class Other(val message: String?) : HealthError
}
