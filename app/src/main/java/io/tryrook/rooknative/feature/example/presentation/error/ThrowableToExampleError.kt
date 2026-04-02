package io.tryrook.rooknative.feature.example.presentation.error

import io.tryrook.rooknative.feature.example.domain.error.ExampleError

fun Throwable.toExampleError(): ExampleError {
    return when (this) {
        is IllegalStateException -> ExampleError.EmptyInput
        else -> ExampleError.Other(localizedMessage ?: message ?: this.toString())
    }
}
