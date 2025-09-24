package io.tryrook.rooknative.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import io.tryrook.rooknative.R
import io.tryrook.rooknative.ui.theme.RookNativeTheme

class HomeScreenRoot : Screen {
    @Composable
    override fun Content() {
        val lifecycleOwner = LocalLifecycleOwner.current

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            println("ON RESUME----------------------------------------------------HomeScreen2")
        }

        DisposableEffect(key1 = lifecycleOwner) {
            val lifecycleEventObserver = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    println("ON RESUME----------------------------------------------------HomeScreen")
                }
            }

            lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
            }
        }

        HomeScreen()
    }
}

@Composable
private fun HomeScreen() {
    println("------------------------------------------------------------------HomeScreen")

    LaunchedEffect(Unit) {
        println("Launched----------------------------------------------------------HomeScreen")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(R.string.welcome))
    }
}

@Preview
@Composable
private fun HomePreview() {
    RookNativeTheme {
        Surface {
            HomeScreen()
        }
    }
}
