package io.tryrook.rooknative.feature.home.presentation.domain.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.tryrook.rooknative.R

enum class HomeTab(@StringRes val nameRes: Int, @DrawableRes val iconRes: Int) {
    SUMMARIES(R.string.summaries, R.drawable.svg_home),
    SETTINGS(R.string.settings, R.drawable.ic_settings),
}
