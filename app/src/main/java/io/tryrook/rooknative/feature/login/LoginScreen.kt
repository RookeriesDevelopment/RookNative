package io.tryrook.rooknative.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.HorizontalExpandedSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.feature.home.HomeScreenRoot
import io.tryrook.rooknative.ui.theme.RookNativeTheme

class LoginScreenRoot : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            println("ON RESUME----------------------------------------------------LoginScreen2")
        }

        DisposableEffect(key1 = lifecycleOwner) {
            val lifecycleEventObserver = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    println("ON RESUME----------------------------------------------------LoginScreen")
                } else {
                    println("UNHANDLED: ${event.name}")
                }
            }

            lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
            }
        }

        LoginScreen(
            toHome = { navigator.push(HomeScreenRoot()) }
        )
    }
}

@Composable
private fun LoginScreen(toHome: () -> Unit) {
    println("------------------------------------------------------------------LoginScreen")

    LaunchedEffect(Unit) {
        println("Launched----------------------------------------------------------LoginScreen")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(96.dp),
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )

        VerticalSpacer(of = 12.dp)

        Text(
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.sign_in_to_continue),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )

        VerticalSpacer(of = 20.dp)

        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            value = "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.email)) }
        )
        Row {
            HorizontalExpandedSpacer()
            TextButton(
                onClick = {},
                content = { Text(text = stringResource(R.string.forget_password)) }
            )
        }

        VerticalSpacer(of = 20.dp)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = toHome,
            content = { Text(text = stringResource(R.string.sign_in)) }
        )
    }
}

@Preview
@Composable
private fun LoginPreview() {
    RookNativeTheme {
        Surface {
            LoginScreen(
                toHome = {}
            )
        }
    }
}
