package io.tryrook.rooknative.feature.healthconnect.presentation.screen

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import io.tryrook.rooknative.core.presentation.component.HorizontalSpacer
import io.tryrook.rooknative.core.presentation.component.MessageAndButton
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.healthconnect.domain.enums.HealthConnectStatus
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectAction
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectEvent
import io.tryrook.rooknative.feature.healthconnect.domain.model.HealthConnectState
import kotlinx.coroutines.flow.collectLatest

class HealthConnectScreenDestination : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<HealthConnectViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    when (it) {
                        HealthConnectEvent.BackgroundSyncEnabled -> {
                            navigator.pop()
                        }

                        HealthConnectEvent.MissingPermissions -> {
                            context.toastLong(R.string.health_connect_all_permissions_denied)
                        }

                        HealthConnectEvent.MissingBackgroundPermissions -> {
                            context.toastLong(R.string.health_connect_background_permissions_denied)
                        }

                        HealthConnectEvent.MissingDataTypesPermissions -> {
                            context.toastLong(R.string.health_connect_data_permissions_denied)
                        }
                    }
                }
            }
        }

        DisposableEffect(key1 = context) {
            val healthConnectBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(receiverContext: Context?, intent: Intent?) {
                    val allPermissions = intent?.getBooleanExtra(
                        /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_PERMISSIONS_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    val partialPermissions = intent?.getBooleanExtra(
                        /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_PERMISSIONS_PARTIALLY_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    val dataTypesPermissions = allPermissions || partialPermissions

                    val backgroundPermissions = intent?.getBooleanExtra(
                        /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_BACKGROUND_PERMISSION_GRANTED,
                        /* defaultValue = */ false
                    ) == true

                    viewModel.onAction(
                        HealthConnectAction.OnPermissionsChanged(
                            dataTypesPermissions = dataTypesPermissions,
                            backgroundPermissions = backgroundPermissions,
                        )
                    )
                }
            }

            ContextCompat.registerReceiver(
                context,
                healthConnectBroadcastReceiver,
                IntentFilter(RookPermissionsManager.ACTION_HEALTH_CONNECT_PERMISSIONS),
                ContextCompat.RECEIVER_EXPORTED,
            )

            onDispose {
                context.unregisterReceiver(healthConnectBroadcastReceiver)
            }
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            viewModel.onAction(HealthConnectAction.OnResume)
        }

        HealthConnectScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
private fun HealthConnectScreen(
    state: HealthConnectState,
    onAction: (HealthConnectAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    var dataTypesExpanded by remember { mutableStateOf(false) }
    var backgroundExpanded by remember { mutableStateOf(false) }

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
            painter = painterResource(R.drawable.svg_health_connect),
            contentDescription = null,
        )
        VerticalSpacer(of = 24.dp)
        Text(
            text = stringResource(R.string.hc_onboarding_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
            ),
        )
        VerticalSpacer(of = 8.dp)
        Text(
            text = stringResource(R.string.hc_onboarding_description),
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
                        if (state.dataTypesGranted)
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Sharp.Check,
                                contentDescription = stringResource(R.string.granted),
                            )
                        if (state.dataTypesGranted)
                            HorizontalSpacer(of = 8.dp)
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
                TextButton(
                    onClick = { backgroundExpanded = !backgroundExpanded },
                    content = {
                        if (state.backgroundGranted)
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Sharp.Check,
                                contentDescription = stringResource(R.string.granted),
                            )
                        if (state.backgroundGranted)
                            HorizontalSpacer(of = 8.dp)
                        Text(text = stringResource(R.string.hc_background_access))
                        HorizontalSpacer(of = 8.dp)
                        if (backgroundExpanded)
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowUp,
                                contentDescription = stringResource(R.string.click_to_shrink),
                            )
                        if (!backgroundExpanded)
                            Icon(
                                imageVector = Icons.Sharp.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.click_to_expand),
                            )
                    },
                )
                AnimatedVisibility(visible = backgroundExpanded) {
                    Column(
                        modifier = Modifier.width(width * 0.75F),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.hc_background_access_justification),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        VerticalSpacer(of = 8.dp)
                        Text(
                            text = stringResource(R.string.hc_background_access_instructions),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        VerticalSpacer(of = 8.dp)
                        Text(
                            text = stringResource(R.string.hc_background_access_instructions_2),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
        VerticalSpacer(of = 20.dp)
        when (state.healthConnectStatus) {
            HealthConnectStatus.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp,
                )
            }

            HealthConnectStatus.NOT_SUPPORTED -> {
                Text(
                    text = stringResource(R.string.hc_not_supported),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            HealthConnectStatus.NOT_INSTALLED -> {
                MessageAndButton(
                    messageRes = R.string.hc_needs_install,
                    buttonRes = R.string.download,
                    onClick = { onAction(HealthConnectAction.OnDownloadClick) },
                )
            }

            HealthConnectStatus.BACKGROUND_NOT_SUPPORTED -> {
                Text(
                    text = stringResource(R.string.hc_background_not_supported),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            HealthConnectStatus.READY -> {
                AnimatedVisibility(visible = state.showAllowAccessButton) {
                    Button(
                        shape = MaterialTheme.shapes.medium,
                        onClick = { onAction(HealthConnectAction.OnAllowAccessClick) },
                        content = { Text(text = stringResource(R.string.allow_access)) },
                    )
                }
                AnimatedVisibility(visible = state.dataTypesGranted && state.backgroundGranted) {
                    Button(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(top = 12.dp),
                        onClick = { onAction(HealthConnectAction.OnConnectClick) },
                        content = { Text(text = stringResource(R.string.connect)) },
                    )
                }
                AnimatedVisibility(state.showOpenSettingsButton) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        VerticalSpacer(of = 12.dp)
                        Text(
                            text = stringResource(R.string.hc_permissions_denied_warning),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                        )
                        VerticalSpacer(of = 8.dp)
                        Button(
                            shape = MaterialTheme.shapes.medium,
                            onClick = { onAction(HealthConnectAction.OnOpenSettingsClick) },
                            content = { Text(text = stringResource(R.string.health_connect_settings)) },
                        )
                    }
                }
            }


        }
    }
}

@PreviewLightDark
@Composable
private fun HealthConnectPreview() {
    RookNativeTheme {
        Surface {
            HealthConnectScreen(
                state = HealthConnectState(
                    dataTypesGranted = true,
                    backgroundGranted = true,
                    showAllowAccessButton = true,
                    showOpenSettingsButton = true,
                    healthConnectStatus = HealthConnectStatus.READY,
                ),
                onAction = {},
            )
        }
    }
}

private val dataTypes = listOf(
    "Active Calories Burned",
    "Basal body temperature",
    "Blood Glucose",
    "Blood Pressure",
    "Body fat",
    "Body Temperature",
    "Body water mass",
    "Bone mass",
    "Distance",
    "Elevation Gained",
    "Exercise",
    "Floors Climbed",
    "Heart Rate",
    "Heart Rate Variability",
    "Height",
    "Hydration",
    "Lean body mass",
    "Nutrition",
    "Oxygen Saturation",
    "Power",
    "Respiratory Rate",
    "Resting Heart Rate",
    "Sleep",
    "Speed",
    "Steps",
    "Total Calories Burned",
    "VO2 Max",
    "Weight",
)
