package io.tryrook.rooknative.core.domain.extension

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.to2Decimals(): Double {
    return BigDecimal(this)
        .setScale(2, RoundingMode.HALF_UP)
        .toDouble()
}
