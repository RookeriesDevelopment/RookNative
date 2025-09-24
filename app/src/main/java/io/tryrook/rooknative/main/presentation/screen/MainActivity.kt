package io.tryrook.rooknative.main.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlusOne
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import io.tryrook.rooknative.feature.login.LoginScreenRoot
import io.tryrook.rooknative.ui.theme.RookNativeTheme

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
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                content = {
                    Box(modifier = Modifier.padding(it)) {
                        Navigator(LoginScreenRoot()) { navigator ->
                            SlideTransition(navigator)
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {},
                        content = { Icon(Icons.Rounded.PlusOne, contentDescription = null) }
                    )
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
