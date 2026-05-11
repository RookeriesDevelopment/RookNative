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
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
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
                println("Scaffold Changed to: ${currentLifecycleState.name}")
            }

            LifecycleResumeEffect(Unit) {
                println("----------------------------------------------------")
                println("LifecycleResumeEffect-Scaffold----------------------------------------------------")

                onPauseOrDispose {
                    println("----------------------------------------------------")
                    println("onPauseOrDispose-Scaffold----------------------------------------------------")
                }
            }

            LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                println("----------------------------------------------------")
                println("LifecycleEventEffect-ON_RESUME-Scaffold----------------------------------------------------")
            }

            DisposableEffect(key1 = lifecycleOwner) {
                val lifecycleEventObserver = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        println("----------------------------------------------------")
                        println("DisposableEffect-ON_RESUME-Scaffold----------------------------------------------------")
                    } else {
                        println("----------------------------------------------------")
                        println("DisposableEffect-UNHANDLED-Scaffold-${event.name}---------------------------------------------------")
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
                    val removed = backStack.removeLastOrNull()

                    println("Destination: $removed was removed")
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
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
                            println("entry<Login> Changed to: ${currentLifecycleState2.name}")
                        }

                        LoginScreenRoot(
                            viewModel = viewModel,
                            toHome = {
                                backStack.add(Home)
                            }
                        )
                    }
                    entry<Home> {
                        val lifecycleOwner2 = LocalLifecycleOwner.current
                        val stateFlow2 = lifecycleOwner2.lifecycle.currentStateFlow
                        val currentLifecycleState2 by stateFlow2.collectAsState()

                        LaunchedEffect(currentLifecycleState2) {
                            println("----------------------------------------------------")
                            println("entry<Home> Changed to: ${currentLifecycleState2.name}")
                        }

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
