package io.tryrook.rooknative.feature.welcome.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.NextButton
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.login.presentation.screen.LoginScreenDestination

class WelcomeScreenDestination : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WelcomeScreen(
            navigateToLogin = { navigator.push(LoginScreenDestination()) },
        )
    }
}

@Composable
fun WelcomeScreen(navigateToLogin: () -> Unit) {
    val configuration = LocalConfiguration.current

    if (configuration.isPortrait()) {
        WelcomeScreenPortrait(navigateToLogin = navigateToLogin)
    } else {
        WelcomeScreenLandScape(navigateToLogin = navigateToLogin)
    }
}

@Composable
private fun WelcomeScreenPortrait(navigateToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .edgeToEdgePadding()
            .padding(16.dp)
            .drawBehind {
                drawArc(
                    topLeft = Offset(0F, center.y / 2F),
                    useCenter = false,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.60F),
                            Color.White.copy(alpha = 0.30F),
                            Color.White.copy(alpha = 0.15F),
                            Color.White.copy(alpha = 0.0F),
                        ),
                        start = Offset(center.x, center.y / 2),
                        end = Offset(center.x, center.y),
                    ),
                    startAngle = 180F,
                    sweepAngle = 180F,
                    size = Size(size.width, size.height / 2F),
                )
            },
        verticalArrangement = Arrangement.Bottom,
    ) {
        Spacer(modifier = Modifier.weight(0.75F))
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(
            text = stringResource(R.string.welcome_title_2),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        VerticalSpacer(of = 12.dp)
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
        )
        VerticalSpacer(of = 24.dp)
        NextButton(onClick = navigateToLogin)
        Spacer(modifier = Modifier.weight(0.25F))
    }
}

@Composable
private fun WelcomeScreenLandScape(navigateToLogin: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .edgeToEdgePadding()
            .padding(16.dp)
            .drawBehind {
                drawArc(
                    topLeft = Offset(0F, 0F),
                    useCenter = false,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.60F),
                            Color.White.copy(alpha = 0.30F),
                            Color.White.copy(alpha = 0.15F),
                            Color.White.copy(alpha = 0.0F),
                        ),
                        start = Offset(center.x, 0F),
                        end = Offset(center.x, center.y),
                    ),
                    startAngle = 180F,
                    sweepAngle = 180F,
                    size = Size(size.width / 2F, size.height),
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(0.50F),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = stringResource(R.string.welcome_title_2),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            VerticalSpacer(of = 48.dp)
        }
        Column(
            modifier = Modifier.weight(0.50F),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyLarge,
            )
            VerticalSpacer(of = 24.dp)
            NextButton(onClick = navigateToLogin)
        }
    }
}

@PreviewLightDark
@Composable
private fun WelcomePreview() {
    RookNativeTheme {
        Surface {
            WelcomeScreen(navigateToLogin = { })
        }
    }
}
