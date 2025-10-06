package io.tryrook.rooknative.feature.connections.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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
import io.tryrook.rooknative.core.presentation.component.NextButton
import io.tryrook.rooknative.core.presentation.component.PullToRefresh
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.androidsteps.presentation.screen.AndroidStepsScreenDestination
import io.tryrook.rooknative.feature.connections.domain.model.HealthKitType
import io.tryrook.rooknative.feature.connections.presentation.component.ApiConnectionTile
import io.tryrook.rooknative.feature.connections.presentation.component.HealthKitConnectionTile
import io.tryrook.rooknative.feature.healthconnect.presentation.screen.HealthConnectScreenDestination
import io.tryrook.rooknative.feature.home.presentation.screen.HomeScreenDestination
import io.tryrook.rooknative.feature.samsunghealth.presentation.screen.SamsungHealthScreenDestination
import kotlinx.coroutines.flow.collectLatest

data class ConnectionsScreenDestination(val disableNextButton: Boolean) : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<ConnectionsViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {

                    when (it) {
                        is ConnectionsEvent.HealthKitConnectionRequest -> {
                            when (it.healthKitType) {
                                HealthKitType.HEALTH_CONNECT -> {
                                    navigator.push(HealthConnectScreenDestination())
                                }

                                HealthKitType.SAMSUNG_HEALTH -> {
                                    navigator.push(SamsungHealthScreenDestination())
                                }

                                HealthKitType.ANDROID_STEPS -> {
                                    navigator.push(AndroidStepsScreenDestination())
                                }
                            }
                        }

                        ConnectionsEvent.AlreadyConnected -> {
                            context.toastLong(R.string.already_connected)
                        }

                        is ConnectionsEvent.ConnectionError -> {
                            context.toastLong(it.message.asString(context))
                        }

                        ConnectionsEvent.DisconnectionNotSupported -> {
                            context.toastLong(R.string.data_source_disconnection_not_supported)
                        }

                        is ConnectionsEvent.DisconnectionError -> {
                            context.toastLong(it.message.asString(context))
                        }

                        ConnectionsEvent.Disconnected -> {
                            context.toastLong(R.string.data_source_disconnected)
                        }
                    }
                }
            }
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            viewModel.onAction(ConnectionsAction.OnResume)
        }

        ConnectionsScreen(
            state = state,
            onAction = viewModel::onAction,
            disableNextButton = disableNextButton,
            navigateToHome = { navigator.replace(HomeScreenDestination()) },
        )
    }
}

@Composable
fun ConnectionsScreen(
    state: ConnectionsState,
    onAction: (ConnectionsAction) -> Unit,
    disableNextButton: Boolean,
    navigateToHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .edgeToEdgePadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        PullToRefresh(
            onRefresh = { onAction(ConnectionsAction.OnConnectionsRefresh) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.connections_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                VerticalSpacer(of = 32.dp)
                AnimatedVisibility(
                    visible = state.loadingHealthKitConnections || state.loadingApiConnections,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                        content = { CircularProgressIndicator() },
                    )
                }
                for (connection in state.healthKitConnections) {
                    HealthKitConnectionTile(
                        connection = connection,
                        loading = state.loadingHealthKitConnections,
                        onConnect = {
                            onAction(ConnectionsAction.OnConnectClick(it))
                        },
                        onDisconnect = {
                            onAction(ConnectionsAction.OnDisconnectClick(it))
                        },
                    )
                }
                for (connection in state.apiConnections) {
                    ApiConnectionTile(
                        connection = connection,
                        loading = state.loadingApiConnections,
                        onConnect = {
                            onAction(ConnectionsAction.OnConnectClick(it))
                        },
                        onDisconnect = {
                            onAction(ConnectionsAction.OnDisconnectClick(it))
                        },
                    )
                }
            }
        }
        if (!disableNextButton) {
            VerticalSpacer(of = 16.dp)
            NextButton(onClick = navigateToHome)
            VerticalSpacer(of = 8.dp)
        }
    }
}

@PreviewLightDark
@Composable
private fun ConnectionsPreview() {
    RookNativeTheme {
        Surface {
            ConnectionsScreen(
                state = ConnectionsState(),
                onAction = {},
                disableNextButton = true,
                navigateToHome = {}
            )
        }
    }
}
