package io.tryrook.rooknative.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import io.tryrook.rooknative.R
import io.tryrook.rooknative.ui.theme.RookNativeTheme

@Composable
fun HomeScreenRoot(viewModel: HomeViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LifecycleResumeEffect(Unit) {
        println("----------------------------------------------------")
        println("LifecycleResumeEffect-HomeScreenRoot----------------------------------------------------")

        onPauseOrDispose {
            println("----------------------------------------------------")
            println("onPauseOrDispose-HomeScreenRoot----------------------------------------------------")
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        println("----------------------------------------------------")
        println("LifecycleEventEffect-ON_RESUME-HomeScreenRoot----------------------------------------------------")
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                println("----------------------------------------------------")
                println("DisposableEffect-ON_RESUME-HomeScreenRoot----------------------------------------------------")
            } else {
                println("----------------------------------------------------")
                println("DisposableEffect-UNHANDLED-HomeScreenRoot-${event.name}---------------------------------------------------")
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
        }
    }

    LaunchedEffect(Unit) {
        println("----------------------------------------------------")
        println("LaunchedEffect-HomeScreenRoot----------------------------------------------------")
    }

    HomeScreen()
}

@Composable
private fun HomeScreen() {
    LaunchedEffect(Unit) {
        println("----------------------------------------------------")
        println("LaunchedEffect-HomeScreen----------------------------------------------------")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(alpha = 0.5F))
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
