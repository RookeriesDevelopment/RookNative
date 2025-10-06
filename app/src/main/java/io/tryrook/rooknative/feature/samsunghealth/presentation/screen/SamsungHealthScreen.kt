package io.tryrook.rooknative.feature.samsunghealth.presentation.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.framework.extension.toastLong
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.MessageAndButton
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthAction
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthEvent
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungHealthState
import io.tryrook.rooknative.feature.samsunghealth.domain.model.SamsungStatus
import io.tryrook.sdk.samsung.domain.enums.SamsungHealthAvailability
import io.tryrook.sdk.samsung.domain.enums.SamsungHealthPermission
import kotlinx.coroutines.flow.collectLatest

class SamsungHealthScreenDestination : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<SamsungHealthViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    when (it) {
                        SamsungHealthEvent.BackgroundSyncEnabled -> {
                            navigator.pop()
                        }

                        SamsungHealthEvent.MissingPermissions -> {
                            context.toastLong(R.string.samsung_health_permissions_denied)
                        }
                    }
                }
            }
        }

        DisposableEffect(key1 = context) {
            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(receiverContext: Context?, intent: Intent?) {
                    val allPermissions = intent?.getBooleanExtra(
                        /* name = */ SamsungHealthPermission.EXTRA_SAMSUNG_HEALTH_PERMISSIONS_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    val partialPermissions = intent?.getBooleanExtra(
                        /* name = */ SamsungHealthPermission.EXTRA_SAMSUNG_HEALTH_PERMISSIONS_PARTIALLY_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    viewModel.onAction(
                        SamsungHealthAction.OnPermissionsChanged(
                            allPermissions = allPermissions,
                            partialPermissions = partialPermissions,
                        )
                    )
                }
            }

            ContextCompat.registerReceiver(
                context,
                broadcastReceiver,
                IntentFilter(SamsungHealthPermission.ACTION_SAMSUNG_HEALTH_PERMISSIONS),
                ContextCompat.RECEIVER_EXPORTED,
            )

            onDispose {
                context.unregisterReceiver(broadcastReceiver)
            }
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            viewModel.onAction(SamsungHealthAction.OnResume)
        }

        SamsungHealthScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
private fun SamsungHealthScreen(
    state: SamsungHealthState,
    onAction: (SamsungHealthAction) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    var dataTypesExpanded by remember { mutableStateOf(false) }

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
            painter = painterResource(R.drawable.png_samsung_health),
            contentDescription = null,
        )
        VerticalSpacer(of = 24.dp)
        Text(
            text = stringResource(R.string.sh_onboarding_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
            ),
        )
        VerticalSpacer(of = 8.dp)
        Text(
            text = stringResource(R.string.sh_onboarding_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        VerticalSpacer(of = 16.dp)
        ElevatedCard {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextButton(
                    onClick = { dataTypesExpanded = !dataTypesExpanded },
                    content = {
                        if (state.permissionsGranted) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Sharp.Check,
                                contentDescription = stringResource(R.string.granted),
                            )
                            HorizontalSpacer(of = 8.dp)
                        }
                        Text(text = stringResource(R.string.hc_data_types_access))
                        HorizontalSpacer(of = 8.dp)
                        if (dataTypesExpanded)
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowUp,
                                contentDescription = stringResource(R.string.click_to_shrink),
                            )
                        if (!dataTypesExpanded)
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.click_to_expand),
                            )
                    },
                )
                AnimatedVisibility(visible = dataTypesExpanded) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        for (dataType in dataTypes) {
                            Text(
                                modifier = Modifier.padding(bottom = 4.dp),
                                text = dataType,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
        VerticalSpacer(of = 20.dp)
        when (state.samsungStatus) {
            SamsungStatus.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp,
                )
            }

            SamsungStatus.Error -> {
                MessageAndButton(
                    messageRes = R.string.availability_samsung_error,
                    buttonRes = R.string.retry,
                    onClick = { onAction(SamsungHealthAction.OnResume) },
                )
            }

            is SamsungStatus.Loaded -> {
                when (state.samsungStatus.availability) {
                    SamsungHealthAvailability.INSTALLED -> {
                        AnimatedVisibility(visible = state.showAllowAccessButton) {
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                onClick = { onAction(SamsungHealthAction.OnAllowAccessClick) },
                                content = { Text(text = stringResource(R.string.allow_access)) },
                            )
                        }
                        AnimatedVisibility(visible = state.permissionsGranted) {
                            Button(
                                modifier = Modifier.padding(top = 12.dp),
                                shape = MaterialTheme.shapes.medium,
                                onClick = { onAction(SamsungHealthAction.OnConnectClick) },
                                content = { Text(text = stringResource(R.string.connect)) },
                            )
                        }
                    }

                    SamsungHealthAvailability.NOT_INSTALLED -> {
                        MessageAndButton(
                            messageRes = R.string.sh_needs_install,
                            buttonRes = R.string.download,
                            onClick = { onAction(SamsungHealthAction.OnDownloadClick) },
                        )
                    }

                    SamsungHealthAvailability.OUTDATED -> {
                        MessageAndButton(
                            messageRes = R.string.sh_needs_update,
                            buttonRes = R.string.update,
                            onClick = { onAction(SamsungHealthAction.OnDownloadClick) },
                        )
                    }

                    SamsungHealthAvailability.DISABLED -> {
                        MessageAndButton(
                            messageRes = R.string.sh_disabled,
                            buttonRes = R.string.open_settings,
                            onClick = { onAction(SamsungHealthAction.OnOpenSettingsClick) },
                        )
                    }

                    SamsungHealthAvailability.NOT_READY -> {
                        MessageAndButton(
                            messageRes = R.string.sh_not_ready,
                            buttonRes = R.string.open_samsung_health,
                            onClick = { onAction(SamsungHealthAction.OnOpenSamsungHealthClick) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SamsungHealthPreview() {
    RookNativeTheme {
        Surface {
            SamsungHealthScreen(
                state = SamsungHealthState(
                    samsungStatus = SamsungStatus.Loaded(SamsungHealthAvailability.INSTALLED)
                ),
                onAction = {},
            )
        }
    }
}

private val dataTypes = listOf(
    "Activity Summary (Total active calories burned, active time, calories burned and distance)",
    "Blood Glucose",
    "Blood Oxygen",
    "Blood Pressure",
    "Body Composition",
    "Exercise",
    "Exercise Location",
    "Floors Climbed",
    "Heart Rate",
    "Nutrition",
    "Sleep",
    "Steps",
    "Water Intake",
)
