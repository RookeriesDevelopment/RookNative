package io.tryrook.rooknative.main.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.home.presentation.screen.HomeScreenDestination
import io.tryrook.rooknative.feature.postsplash.presentation.screen.PostSplashScreenDestination
import io.tryrook.rooknative.feature.welcome.presentation.screen.WelcomeScreenDestination
import io.tryrook.rooknative.main.domain.enums.UserSessionStatus
import io.tryrook.rooknative.main.domain.model.MainState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.uiState.value.loading }
        }

        setContent {
            RookNativeTheme {
                Surface {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                    MainContent(state)
                }
            }
        }
    }
}

@Composable
private fun MainContent(state: MainState) {
    when (state.userSessionStatus) {
        UserSessionStatus.NOT_LOGGED_IN -> {
            Navigator(
                screen = WelcomeScreenDestination(),
            )
        }

        UserSessionStatus.LOGGED_IN -> {
            Navigator(
                screen = HomeScreenDestination(),
            )
        }

        UserSessionStatus.NONE -> {
            Navigator(
                screen = PostSplashScreenDestination(),
            )
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    RookNativeTheme {
        Surface {
            MainContent(
                state = MainState(
                    loading = false,
                    userSessionStatus = UserSessionStatus.LOGGED_IN,
                )
            )
        }
    }
}
