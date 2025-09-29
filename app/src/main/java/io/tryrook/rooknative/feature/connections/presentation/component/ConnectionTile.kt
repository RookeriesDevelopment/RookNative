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
import coil.compose.AsyncImage
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.domain.model.Connection

@Composable
fun ConnectionTile(modifier: Modifier = Modifier, connection: Connection) {
    when (connection) {
        is Connection.Api -> {
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
                    connected = connection.connected,
                    onClick = {},
                )
            }
        }

        is Connection.HealthKit -> {
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
                    connected = true,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConnectionTilePreview() {
    RookNativeTheme {
        Surface {
            Column {
                ConnectionTile(connection = Connection.HealthKit.buildSamsungHealth())
                VerticalSpacer(of = 8.dp)
                ConnectionTile(
                    connection = Connection.Api(
                        name = "Fitbit",
                        connected = true,
                        iconUrl = "https://zatznotfunny.com/wp-content/uploads/2016/04/fitbit-logo.png"
                    )
                )
            }
        }
    }
}
