package io.tryrook.rooknative.feature.connections.presentation.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.domain.model.Connection

@Composable
fun ApiConnectionTile(
    modifier: Modifier = Modifier,
    connection: Connection.Api,
    loading: Boolean,
    onConnect: (Connection.Api) -> Unit,
    onDisconnect: (Connection.Api) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            model = connection.iconUrl,
            contentDescription = null,
        )
        HorizontalSpacer(of = 16.dp)
        Text(
            modifier = Modifier.weight(1F),
            text = connection.name,
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
private fun ApiConnectionTilePreview() {
    RookNativeTheme {
        Surface {
            Column {
                ApiConnectionTile(
                    connection = Connection.Api(
                        "Oura",
                        false,
                        "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/oura.png"
                    ),
                    loading = false,
                    onConnect = {},
                    onDisconnect = {},
                )
                VerticalSpacer(of = 8.dp)
                ApiConnectionTile(
                    connection = Connection.Api(
                        "Polar",
                        true,
                        "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/polar.png"
                    ),
                    loading = true,
                    onConnect = {},
                    onDisconnect = {},
                )
            }
        }
    }
}
