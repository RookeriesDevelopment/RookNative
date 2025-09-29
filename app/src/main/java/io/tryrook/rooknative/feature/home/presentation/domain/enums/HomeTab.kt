package io.tryrook.rooknative.feature.home.presentation.domain.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cafe.adriel.voyager.core.screen.Screen
import io.tryrook.rooknative.R
import io.tryrook.rooknative.feature.settings.presentation.screen.SettingsScreenDestination
import io.tryrook.rooknative.feature.summaries.presentation.screen.SummariesScreenDestination

enum class HomeTab(@StringRes val nameRes: Int, @DrawableRes val iconRes: Int) {
    SUMMARIES(R.string.summaries, R.drawable.svg_home),
    SETTINGS(R.string.settings, R.drawable.ic_settings), ;

    fun toggle(): HomeTab {
        return when (this) {
            SUMMARIES -> SETTINGS
            SETTINGS -> SUMMARIES
        }
    }

    fun buildDestination(): Screen {
        return when (this) {
            SUMMARIES -> SummariesScreenDestination()
            SETTINGS -> SettingsScreenDestination()
        }
    }
}
