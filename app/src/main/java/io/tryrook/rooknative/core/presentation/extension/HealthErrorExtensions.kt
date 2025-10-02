package io.tryrook.rooknative.core.presentation.extension

import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.error.HealthError.HttpRequestFailed
import io.tryrook.rooknative.core.domain.error.HealthError.HttpRequestTimeout
import io.tryrook.rooknative.core.domain.error.HealthError.Other
import io.tryrook.rooknative.core.domain.error.HealthError.Unauthorized
import io.tryrook.rooknative.core.presentation.text.UIText

fun HealthError.toUiText(): UIText {
    return when (this) {
        HttpRequestTimeout -> UIText.FromResource(R.string.network_error)
        HttpRequestFailed -> UIText.FromResource(R.string.request_failed)
        Unauthorized -> UIText.FromResource(R.string.rook_sdk_unauthorized)
        is Other -> {
            val messageString = message

            if (messageString != null) {
                UIText.FromString(messageString)
            } else {
                UIText.FromResource(R.string.unknown_error)
            }
        }
    }
}
