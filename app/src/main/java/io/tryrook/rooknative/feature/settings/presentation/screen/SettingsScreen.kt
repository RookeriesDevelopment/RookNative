package io.tryrook.rooknative.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.framework.extension.toastLong
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.connections.presentation.screen.ConnectionsScreenDestination
import io.tryrook.rooknative.feature.settings.domain.model.SettingsAction
import io.tryrook.rooknative.feature.settings.domain.model.SettingsEvent
import io.tryrook.rooknative.feature.settings.presentation.component.SettingTile
import io.tryrook.rooknative.feature.welcome.presentation.screen.WelcomeScreenDestination
import kotlinx.coroutines.flow.collectLatest

class SettingsScreenDestination : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow
        val navigatorParent = navigator.parent

        val viewModel = getViewModel<SettingsViewModel>()

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    when (it) {
                        SettingsEvent.Logout -> {
                            context.toastLong(R.string.good_bye)
                            navigatorParent!!.replaceAll(WelcomeScreenDestination())
                        }

                        SettingsEvent.LogoutError -> {
                            context.toastLong(R.string.logout_error)
                        }
                    }
                }
            }
        }

        SettingsScreen(
            onAction = viewModel::onAction,
            navigateToConnections = {
                navigatorParent?.push(ConnectionsScreenDestination(disableNextButton = true))
            },
        )
    }
}

@Composable
fun SettingsScreen(
    onAction: (SettingsAction) -> Unit,
    navigateToConnections: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .edgeToEdgePadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SettingTile(
            modifier = Modifier.padding(8.dp),
            titleRes = R.string.manage_connections,
            onClick = navigateToConnections,
        )
        HorizontalDivider()
        SettingTile(
            modifier = Modifier.padding(8.dp),
            titleRes = R.string.logout,
            iconRes = R.drawable.ic_logout,
            color = MaterialTheme.colorScheme.error,
            onClick = { onAction(SettingsAction.OnLogoutClick) },
        )
        HorizontalDivider()
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    RookNativeTheme {
        Surface {
            SettingsScreen(
                onAction = {},
                navigateToConnections = {},
            )
        }
    }
}
