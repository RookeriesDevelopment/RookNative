package io.tryrook.rooknative.main.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import io.tryrook.rooknative.feature.home.HomeScreenRoot
import io.tryrook.rooknative.feature.home.HomeViewModel
import io.tryrook.rooknative.feature.login.LoginScreenRoot
import io.tryrook.rooknative.feature.login.LoginViewModel
import io.tryrook.rooknative.ui.theme.RookNativeTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainContent() }
    }
}

@Serializable
data object Login : NavKey

@Serializable
data object Home : NavKey

@Composable
private fun MainContent() {
    RookNativeTheme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val backStack = rememberNavBackStack(Login)

            val lifecycleOwner = LocalLifecycleOwner.current
            val stateFlow = lifecycleOwner.lifecycle.currentStateFlow
            val currentLifecycleState by stateFlow.collectAsState()

            LaunchedEffect(currentLifecycleState) {
                println("----------------------------------------------------")
                println("Changed to: ${currentLifecycleState.name}")
            }

            DisposableEffect(key1 = lifecycleOwner) {
                val lifecycleEventObserver = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        println("----------------------------------------------------")
                        println("ON RESUME")
                    } else {
                        println("----------------------------------------------------")
                        println("UNHANDLED: ${event.name}")
                    }
                }

                lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
                }
            }

            NavDisplay(
                modifier = Modifier.padding(innerPadding),
                backStack = backStack,
                onBack = {
                    println("removing: $it destinations")
                    backStack.removeLastOrNull()
                },
                entryDecorators = listOf(
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<Login> {
                        val viewModel = viewModel<LoginViewModel>()

                        val lifecycleOwner2 = LocalLifecycleOwner.current
                        val stateFlow2 = lifecycleOwner2.lifecycle.currentStateFlow
                        val currentLifecycleState2 by stateFlow2.collectAsState()

                        LaunchedEffect(currentLifecycleState2) {
                            println("----------------------------------------------------")
                            println("Changed to2: ${currentLifecycleState2.name}")
                        }

                        LoginScreenRoot(
                            viewModel = viewModel,
                            toHome = {
                                backStack.add(Home)
                            }
                        )
                    }
                    entry<Home> {
                        val viewModel = viewModel<HomeViewModel>()
                        HomeScreenRoot(viewModel)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    MainContent()
}
