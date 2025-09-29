package io.tryrook.rooknative.main.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.welcome.presentation.screen.WelcomeScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainContent() }
    }
}

@Composable
private fun MainContent() {
    RookNativeTheme {
        Surface {
            Navigator(
                screen = WelcomeScreenDestination(),
            )
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    MainContent()
}
