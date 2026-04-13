package io.tryrook.rooknative.feature.example.presentation.error

import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.text.UIText
import io.tryrook.rooknative.feature.example.domain.error.ExampleError

fun ExampleError.toUIText(): UIText {
    return when (this) {
        ExampleError.EmptyInput -> UIText.FromResource(R.string.input_error)
        is ExampleError.Other -> UIText.FromString(message)
    }
}
