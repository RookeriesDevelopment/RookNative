package io.tryrook.rooknative.core.rook

import com.rookmotion.rook.sdk.domain.exception.HCHttpRequestException
import com.rookmotion.rook.sdk.domain.exception.HCMissingConfigurationException
import com.rookmotion.rook.sdk.domain.exception.HCNotAuthorizedException
import com.rookmotion.rook.sdk.domain.exception.HCNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.HCSessionExpiredException
import com.rookmotion.rook.sdk.domain.exception.HCTimeoutException
import io.tryrook.api.sources.domain.exception.ApiHttpRequestException
import io.tryrook.api.sources.domain.exception.ApiNotAuthorizedException
import io.tryrook.api.sources.domain.exception.ApiNotInitializedException
import io.tryrook.api.sources.domain.exception.ApiSessionExpiredException
import io.tryrook.api.sources.domain.exception.ApiTimeoutException
import io.tryrook.sdk.samsung.domain.exception.SHHttpRequestException
import io.tryrook.sdk.samsung.domain.exception.SHNotAuthorizedException
import io.tryrook.sdk.samsung.domain.exception.SHNotInitializedException
import io.tryrook.sdk.samsung.domain.exception.SHSessionExpiredException
import io.tryrook.sdk.samsung.domain.exception.SHTimeoutException

fun Throwable.toHealthError(): HealthError {
    return when (this) {
        is HCMissingConfigurationException -> HealthError.Unauthorized
        is HCNotInitializedException -> HealthError.Unauthorized
        is HCNotAuthorizedException -> HealthError.Unauthorized
        is HCSessionExpiredException -> HealthError.Unauthorized
        is HCHttpRequestException -> HealthError.HttpRequestFailed
        is HCTimeoutException -> HealthError.HttpRequestTimeout

        is SHNotInitializedException -> HealthError.Unauthorized
        is SHNotAuthorizedException -> HealthError.Unauthorized
        is SHSessionExpiredException -> HealthError.Unauthorized
        is SHHttpRequestException -> HealthError.HttpRequestFailed
        is SHTimeoutException -> HealthError.HttpRequestTimeout

        is ApiNotInitializedException -> HealthError.Unauthorized
        is ApiNotAuthorizedException -> HealthError.Unauthorized
        is ApiSessionExpiredException -> HealthError.Unauthorized
        is ApiHttpRequestException -> HealthError.HttpRequestFailed
        is ApiTimeoutException -> HealthError.HttpRequestTimeout
        else -> HealthError.Other(message)
    }
}
