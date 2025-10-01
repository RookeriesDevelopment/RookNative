package io.tryrook.rooknative.core.domain.error

sealed interface HealthError : Error {
    data object HttpRequestTimeout : HealthError
    data object HttpRequestFailed : HealthError
    data object Unauthorized : HealthError
    data class Other(val message: String?) : HealthError
}
