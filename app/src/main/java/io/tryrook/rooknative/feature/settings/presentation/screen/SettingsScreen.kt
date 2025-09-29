package io.tryrook.rooknative.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.settings.presentation.component.SettingTile

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .edgeToEdgePadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
    ) {
        SettingTile(
            modifier = Modifier.padding(8.dp),
            titleRes = R.string.manage_connections,
        )
        HorizontalDivider()
        SettingTile(
            modifier = Modifier.padding(8.dp),
            titleRes = R.string.logout,
            iconRes = R.drawable.ic_logout,
            color = MaterialTheme.colorScheme.error,
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    RookNativeTheme {
        Surface {
            SettingsScreen()
        }
    }
}
