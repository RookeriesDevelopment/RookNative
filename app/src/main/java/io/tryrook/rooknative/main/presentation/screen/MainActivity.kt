package io.tryrook.rooknative.main.presentation.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.tryrook.rooknative.feature.welcome.WelcomeScreen
import io.tryrook.rooknative.ui.theme.RookNativeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainContent() }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MainContent() {
    RookNativeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            WelcomeScreen()
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    MainContent()
}
