package io.tryrook.rooknative.feature.postsplash.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.VerticalExpandedSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

class PostSplashScreenDestination : Screen {
    @Composable
    override fun Content() {
        PostSplashScreen()
    }
}

@Composable
fun PostSplashScreen() {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        VerticalExpandedSpacer()
        Image(
            modifier = Modifier.height(80.dp),
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onSurface
            ),
            contentScale = ContentScale.FillHeight
        )
        VerticalSpacer(of = 32.dp)
        LinearProgressIndicator(
            modifier = Modifier
                .height(12.dp)
                .then(
                    if (configuration.isPortrait()) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    }
                ),
            strokeCap = StrokeCap.Round,
        )
        VerticalExpandedSpacer()
    }
}

@PreviewLightDark
@Composable
private fun PostSplashPreview() {
    RookNativeTheme {
        Surface {
            PostSplashScreen()
        }
    }
}
