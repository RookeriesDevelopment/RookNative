package io.tryrook.rooknative.core.framework.error

import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.SDKNotAuthorizedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.sdk.samsung.domain.exception.SHHttpRequestException
import io.tryrook.sdk.samsung.domain.exception.SHNotAuthorizedException
import io.tryrook.sdk.samsung.domain.exception.SHTimeoutException

fun Throwable.toHealthError(): HealthError {
    return when (this) {
        is HttpRequestException -> HealthError.HttpRequestFailed
        is SHHttpRequestException -> HealthError.HttpRequestFailed
        is TimeoutException -> HealthError.HttpRequestTimeout
        is SHTimeoutException -> HealthError.HttpRequestTimeout
        is SDKNotAuthorizedException -> HealthError.Unauthorized
        is SHNotAuthorizedException -> HealthError.Unauthorized
        else -> HealthError.Other(message)
    }
}
