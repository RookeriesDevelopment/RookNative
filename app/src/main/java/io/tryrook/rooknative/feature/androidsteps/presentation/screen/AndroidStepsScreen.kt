package io.tryrook.rooknative.feature.androidsteps.presentation.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rookmotion.rook.sdk.RookPermissionsManager
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.framework.extension.toastLong
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsAction
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsEvent
import io.tryrook.rooknative.feature.androidsteps.domain.model.AndroidStepsState
import kotlinx.coroutines.flow.collectLatest

class AndroidStepsScreenDestination : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<AndroidStepsViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    when (it) {
                        AndroidStepsEvent.AndroidStepsEnabled -> {
                            navigator.pop()
                        }

                        AndroidStepsEvent.MissingPermissions -> {
                            context.toastLong(R.string.android_permissions_denied)
                        }
                    }
                }
            }
        }

        DisposableEffect(key1 = context) {
            val androidBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(intentContext: Context?, intent: Intent?) {
                    val permissionsGranted = intent?.getBooleanExtra(
                        /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    val dialogDisplayed = intent?.getBooleanExtra(
                        /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_DIALOG_DISPLAYED,
                        /* defaultValue = */ false
                    ) == true

                    viewModel.onAction(
                        AndroidStepsAction.OnPermissionsChanged(
                            permissionsGranted = permissionsGranted,
                            shouldRequestPermissions = dialogDisplayed
                        )
                    )

                    if (!permissionsGranted) {
                        context.toastLong(R.string.android_permissions_denied)
                    }
                }
            }

            ContextCompat.registerReceiver(
                context,
                androidBroadcastReceiver,
                IntentFilter(RookPermissionsManager.ACTION_ANDROID_PERMISSIONS),
                ContextCompat.RECEIVER_EXPORTED,
            )

            onDispose {
                context.unregisterReceiver(androidBroadcastReceiver)
            }
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            viewModel.onAction(AndroidStepsAction.OnResume)
        }

        AndroidStepsScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
private fun AndroidStepsScreen(state: AndroidStepsState, onAction: (AndroidStepsAction) -> Unit) {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(width / 3),
            painter = painterResource(R.drawable.svg_android),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.as_onboarding_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
            ),
        )
        VerticalSpacer(of = 24.dp)
        Text(
            text = stringResource(R.string.as_onboarding_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        VerticalSpacer(of = 12.dp)
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.notifications),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.physical_activity),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        VerticalSpacer(of = 20.dp)
        AnimatedVisibility(visible = state.showGrantPermissionsButton) {
            Button(
                shape = MaterialTheme.shapes.medium,
                onClick = { onAction(AndroidStepsAction.OnGrantPermissionsClick) },
                content = { Text(text = stringResource(R.string.grant_permissions)) },
            )
        }
        AnimatedVisibility(visible = state.permissionsGranted) {
            Button(
                modifier = Modifier.padding(top = 12.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = { onAction(AndroidStepsAction.OnConnectClick) },
                content = { Text(text = stringResource(R.string.connect)) },
            )
        }
        AnimatedVisibility(visible = state.showOpenSettingsButton) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VerticalSpacer(of = 16.dp)
                Text(
                    text = stringResource(R.string.android_permissions_denied_warning),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                VerticalSpacer(of = 12.dp)
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = { onAction(AndroidStepsAction.OnOpenSettingsClick) },
                    content = { Text(text = stringResource(R.string.open_settings)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun AndroidStepsPreview() {
    RookNativeTheme {
        Surface {
            AndroidStepsScreen(
                state = AndroidStepsState(
                    permissionsGranted = false,
                    shouldRequestPermissions = false,
                    showGrantPermissionsButton = true,
                    showOpenSettingsButton = true,
                ),
                onAction = {},
            )
        }
    }
}
