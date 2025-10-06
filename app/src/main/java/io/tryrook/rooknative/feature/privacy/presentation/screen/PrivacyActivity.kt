@file:OptIn(ExperimentalMaterial3Api::class)

package io.tryrook.rooknative.feature.privacy.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dagger.hilt.android.AndroidEntryPoint
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@AndroidEntryPoint
class PrivacyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RookNativeTheme {
                PrivacyContent()
            }
        }
    }

    @Composable
    fun PrivacyContent() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(R.string.privacy_policy)) },
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Your privacy policy goes here",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }

    @PreviewLightDark
    @Composable
    private fun PrivacyContentPreview() {
        RookNativeTheme {
            PrivacyContent()
        }
    }
}
