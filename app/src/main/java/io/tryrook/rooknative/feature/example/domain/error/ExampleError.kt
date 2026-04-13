package io.tryrook.rooknative.feature.example.domain.error

import io.tryrook.rooknative.core.domain.error.Error

sealed interface ExampleError : Error {
    data object EmptyInput : ExampleError
    data class Other(val message: String) : ExampleError
}


