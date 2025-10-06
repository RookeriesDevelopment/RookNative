package io.tryrook.rooknative.feature.connections.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.domain.model.Connection

@Composable
fun HealthKitConnectionTile(
    modifier: Modifier = Modifier,
    connection: Connection.HealthKit,
    loading: Boolean,
    onConnect: (Connection.HealthKit) -> Unit,
    onDisconnect: (Connection.HealthKit) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            painter = painterResource(connection.iconRes),
            contentDescription = null,
        )
        HorizontalSpacer(of = 16.dp)
        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(connection.nameRes)
        )
        ConnectionButton(
            enabled = !loading,
            connected = connection.connected,
            onConnect = { onConnect(connection) },
            onDisconnect = { onDisconnect(connection) }
        )
    }
}

@Preview
@Composable
private fun HealthKitConnectionTilePreview() {
    RookNativeTheme {
        Surface {
            Column {
                HealthKitConnectionTile(
                    connection = Connection.HealthKit.buildSamsungHealth(connected = true),
                    loading = false,
                    onConnect = {},
                    onDisconnect = {},
                )
                VerticalSpacer(of = 8.dp)
                HealthKitConnectionTile(
                    connection = Connection.HealthKit.buildSamsungHealth(connected = true),
                    loading = true,
                    onConnect = {},
                    onDisconnect = {},
                )
            }
        }
    }
}
