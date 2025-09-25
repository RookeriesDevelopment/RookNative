package io.tryrook.rooknative.core.presentation.extension

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Configuration.isPortrait(): Boolean {
    return orientation == Configuration.ORIENTATION_PORTRAIT
}

@Stable
fun Configuration.isLandscape(): Boolean {
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}

@ReadOnlyComposable
@Composable
@Stable
fun Configuration.calculateWidthDp(windowInsets: WindowInsets): Dp {
    return if (isPortrait()) {
        screenWidthDp.dp
    } else {
        val direction = LocalLayoutDirection.current
        val windowInsetsPadding = windowInsets.asPaddingValues()

        val startPadding = windowInsetsPadding.calculateStartPadding(direction)
        val endPadding = windowInsetsPadding.calculateEndPadding(direction)

        screenWidthDp.dp - (startPadding + endPadding)
    }
}
