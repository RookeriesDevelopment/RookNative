package io.tryrook.rooknative.feature.welcome.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@Composable
fun WelcomeScreen() {
    val configuration = LocalConfiguration.current

    if (configuration.isPortrait()) {
        WelcomeScreenPortrait()
    } else {
        WelcomeScreenLandScape()
    }
}

@Composable
private fun WelcomeScreenPortrait() {
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
        FilledIconButton(
            modifier = Modifier.size(60.dp),
            shape = MaterialTheme.shapes.large,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
            ),
            onClick = {},
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = stringResource(R.string.next),
                    tint = MaterialTheme.colorScheme.primaryContainer,
                )
            },
        )
        Spacer(modifier = Modifier.weight(0.25F))
    }
}

@Composable
private fun WelcomeScreenLandScape() {
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
            FilledIconButton(
                modifier = Modifier.size(60.dp),
                shape = MaterialTheme.shapes.large,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                ),
                onClick = {},
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = stringResource(R.string.next),
                        tint = MaterialTheme.colorScheme.primaryContainer,
                    )
                },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WelcomePreview() {
    RookNativeTheme {
        Surface {
            WelcomeScreen()
        }
    }
}
