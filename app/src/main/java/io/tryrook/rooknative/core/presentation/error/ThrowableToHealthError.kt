package io.tryrook.rooknative.core.presentation.error

import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.SDKNotAuthorizedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.error.HealthError.HttpRequestFailed
import io.tryrook.rooknative.core.domain.error.HealthError.HttpRequestTimeout
import io.tryrook.rooknative.core.domain.error.HealthError.Other
import io.tryrook.rooknative.core.domain.error.HealthError.Unauthorized
import io.tryrook.sdk.samsung.domain.exception.SHHttpRequestException
import io.tryrook.sdk.samsung.domain.exception.SHNotAuthorizedException
import io.tryrook.sdk.samsung.domain.exception.SHTimeoutException

fun Throwable.toHealthError(): HealthError {
    return when (this) {
        is HttpRequestException -> HttpRequestFailed
        is SHHttpRequestException -> HttpRequestFailed
        is TimeoutException -> HttpRequestTimeout
        is SHTimeoutException -> HttpRequestTimeout
        is SDKNotAuthorizedException -> Unauthorized
        is SHNotAuthorizedException -> Unauthorized
        else -> Other(message)
    }
}
