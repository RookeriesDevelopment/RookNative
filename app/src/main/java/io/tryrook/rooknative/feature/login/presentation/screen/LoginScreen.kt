package io.tryrook.rooknative.feature.login.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.presentation.screen.ConnectionsScreenDestination

class LoginScreenDestination : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LoginScreen(
            navigateToConnections = {
                navigator.push(ConnectionsScreenDestination(disableNextButton = false))
            },
        )
    }
}

@Composable
fun LoginScreen(navigateToConnections: () -> Unit) {
    val configuration = LocalConfiguration.current

    if (configuration.isPortrait()) {
        LoginScreenPortrait(navigateToConnections = navigateToConnections)
    } else {
        LoginScreenLandscape(navigateToConnections = navigateToConnections)
    }
}

@Composable
private fun LoginScreenPortrait(
    navigateToConnections: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.png_login_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        ElevatedCard(
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp),
            ),
            content = {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .navigationBarsPadding(),
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    VerticalSpacer(of = 24.dp)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        value = "",
                        onValueChange = {},
                        label = { Text(text = stringResource(R.string.login_button)) }
                    )
                    VerticalSpacer(of = 20.dp)
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = navigateToConnections,
                        content = {
                            Text(text = stringResource(R.string.next))
                            HorizontalSpacer(of = 8.dp)
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null
                            )
                        }
                    )
                    VerticalSpacer(of = 24.dp)
                }
            }
        )
    }
}

@Composable
private fun LoginScreenLandscape(
    navigateToConnections: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier
                .weight(0.5F)
                .fillMaxHeight(),
            painter = painterResource(R.drawable.png_login_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
        Column(
            modifier = Modifier
                .weight(0.5F)
                .fillMaxHeight()
                .padding(20.dp)
                .edgeToEdgePadding(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            VerticalSpacer(of = 24.dp)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.login_button)) }
            )
            VerticalSpacer(of = 20.dp)
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                onClick = navigateToConnections,
                content = {
                    Text(text = stringResource(R.string.next))
                    HorizontalSpacer(of = 8.dp)
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null
                    )
                }
            )
            VerticalSpacer(of = 24.dp)
        }
    }
}

@PreviewLightDark
@Composable
private fun LoginPreview() {
    RookNativeTheme {
        Surface {
            LoginScreen(navigateToConnections = {})
        }
    }
}
