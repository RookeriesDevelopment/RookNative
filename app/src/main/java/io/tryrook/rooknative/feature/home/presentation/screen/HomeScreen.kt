@file:OptIn(ExperimentalMaterial3Api::class)

package io.tryrook.rooknative.feature.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.home.presentation.domain.enums.HomeTab
import io.tryrook.rooknative.feature.summaries.presentation.screen.SummariesScreenDestination

class HomeScreenDestination : Screen {
    @Composable
    override fun Content() {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.SUMMARIES) }

    Navigator(
        screen = SummariesScreenDestination(),
        onBackPressed = {
            selectedTab = selectedTab.toggle()
            true
        }
    ) { navigator ->
        Scaffold(
            bottomBar = {
                PrimaryTabRow(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(vertical = 8.dp),
                    selectedTabIndex = selectedTab.ordinal,
                    indicator = {},
                    divider = {},
                    tabs = {
                        HomeTab.entries.forEach { tab ->
                            Tab(
                                selected = selectedTab == tab,
                                onClick = {
                                    navigator.replace(tab.buildDestination())
                                    selectedTab = tab
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(tab.iconRes),
                                        contentDescription = stringResource(tab.nameRes),
                                        tint = if (selectedTab == tab) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.outline
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CurrentScreen()
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun HomePreview() {
    RookNativeTheme {
        Surface {
            HomeScreen()
        }
    }
}
