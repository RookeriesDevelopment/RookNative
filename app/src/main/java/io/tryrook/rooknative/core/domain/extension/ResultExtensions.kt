package io.tryrook.rooknative.core.domain.extension

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.tryrook.rooknative.core.domain.error.Error

inline fun <L : Error, R> Result<R>.toEither(transformingThrowableTo: (Throwable) -> L): Either<L, R> {
    return fold(
        onSuccess = { it.right() },
        onFailure = { transformingThrowableTo(it).left() }
    )
}
