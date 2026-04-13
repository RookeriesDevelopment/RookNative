package io.tryrook.rooknative.core.rook

import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.text.UIText

fun HealthError.toUiText(): UIText {
    return when (this) {
        HealthError.HttpRequestTimeout -> UIText.FromResource(R.string.network_error)
        HealthError.HttpRequestFailed -> UIText.FromResource(R.string.request_failed)
        HealthError.Unauthorized -> UIText.FromResource(R.string.rook_sdk_unauthorized)
        is HealthError.Other -> {
            val messageString = message

            if (messageString != null) {
                UIText.FromString(messageString)
            } else {
                UIText.FromResource(R.string.unknown_error)
            }
        }
    }
}
