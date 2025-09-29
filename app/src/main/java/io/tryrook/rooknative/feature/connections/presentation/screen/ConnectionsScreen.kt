package io.tryrook.rooknative.feature.connections.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.NextButton
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.domain.model.Connection
import io.tryrook.rooknative.feature.connections.presentation.component.ConnectionTile
import io.tryrook.rooknative.feature.home.presentation.screen.HomeScreenDestination

val state = ConnectionsState(
    connections = buildList {
        add(Connection.HealthKit.buildHealthConnect())
        add(Connection.HealthKit.buildSamsungHealth())
        add(Connection.HealthKit.buildAndroid())
        add(
            Connection.Api(
                "Oura",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/oura.png"
            )
        )
        add(
            Connection.Api(
                "Polar",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/polar.png"
            )
        )
        add(
            Connection.Api(
                "Whoop",
                true,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/whoop.png"
            )
        )
        add(
            Connection.Api(
                "Dexcom",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/dexcom.png"
            )
        )
        add(
            Connection.Api(
                "Fitbit",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/fitbit.png"
            )
        )
        add(
            Connection.Api(
                "Garmin",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/garmin.png"
            )
        )
        add(
            Connection.Api(
                "Withings",
                true,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/withings.png"
            )
        )
        add(
            Connection.Api(
                "Strava",
                false,
                "https://api-media-root.s3.us-east-2.amazonaws.com/static/img/strava.png"
            )
        )
    }
)

data class ConnectionsScreenDestination(val disableNextButton: Boolean) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ConnectionsScreen(
            disableNextButton = disableNextButton,
            navigateToHome = { navigator.replace(HomeScreenDestination()) },
        )
    }
}

@Composable
fun ConnectionsScreen(disableNextButton: Boolean, navigateToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .edgeToEdgePadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.connections_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            VerticalSpacer(of = 32.dp)
            for (connection in state.connections) {
                ConnectionTile(connection = connection)
            }
        }
        if (!disableNextButton) {
            VerticalSpacer(of = 16.dp)
            NextButton(onClick = navigateToHome)
            VerticalSpacer(of = 8.dp)
        }
    }
}

@PreviewLightDark
@Composable
private fun ConnectionsPreview() {
    RookNativeTheme {
        Surface {
            ConnectionsScreen(disableNextButton = true, navigateToHome = {})
        }
    }
}
