package io.tryrook.rooknative.feature.connections.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@Composable
fun ConnectionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    connected: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (connected) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                Color(0xFFA0E984)
            },
            contentColor = if (connected) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                Color.Black
            },
        ),
        onClick = {
            if (connected) {
                onDisconnect()
            } else {
                onConnect()
            }
        },
        content = {
            if (connected) {
                Text(text = stringResource(R.string.disconnect))
            } else {
                Text(text = stringResource(R.string.connect))
            }
        }
    )
}

@Preview
@Composable
private fun ConnectionButtonPreview() {
    RookNativeTheme {
        Surface {
            Column {
                ConnectionButton(connected = true, onConnect = {}, onDisconnect = {})
                ConnectionButton(connected = false, onConnect = {}, onDisconnect = {})
            }
        }
    }
}
