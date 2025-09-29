package io.tryrook.rooknative.feature.settings.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.HorizontalExpandedSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@Composable
fun SettingTile(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int = R.drawable.ic_chevron_right,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.bodyLarge.copy(color = color),
        )
        HorizontalExpandedSpacer()
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = color,
        )
    }
}

@Preview
@Composable
private fun SettingTilePreview() {
    RookNativeTheme {
        Surface {
            SettingTile(titleRes = R.string.settings, onClick = {})
        }
    }
}
