package io.tryrook.rooknative.core.data.repository

import arrow.core.Either
import io.tryrook.rooknative.core.domain.repository.ExampleRepository
import io.tryrook.rooknative.feature.example.domain.error.ExampleError
import io.tryrook.rooknative.feature.example.presentation.error.toExampleError
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class DefaultExampleRepository : ExampleRepository {
    val data = mutableListOf("DATA_1", "DATA_2", "DATA_3")

    override suspend fun getData(): Either<ExampleError, List<String>> {
        return Either.catch {
            delay(1.5.seconds)
            data
        }.mapLeft {
            it.toExampleError()
        }
    }

    override suspend fun submitData(data: String): Either<ExampleError, Unit> {
        return Either.catch {
            delay(1.seconds)
            this.data.add(data)

            Unit
        }.mapLeft {
            it.toExampleError()
        }
    }
}
