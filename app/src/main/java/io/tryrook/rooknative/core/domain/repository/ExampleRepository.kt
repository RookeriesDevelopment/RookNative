package io.tryrook.rooknative.core.domain.repository

import arrow.core.Either
import io.tryrook.rooknative.feature.example.domain.error.ExampleError

interface ExampleRepository {
    suspend fun getData(): Either<ExampleError, List<String>>
    suspend fun submitData(data: String): Either<ExampleError, Unit>
}
