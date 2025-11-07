@file:OptIn(ExperimentalLayoutApi::class)

package io.tryrook.rooknative.feature.summaries.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.PullToRefresh
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.summaries.domain.model.SummariesAction
import io.tryrook.rooknative.feature.summaries.domain.model.SummariesState
import io.tryrook.rooknative.feature.summaries.domain.model.Summary
import io.tryrook.rooknative.feature.summaries.presentation.component.SummaryCard

class SummariesScreenDestination : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<SummariesViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        SummariesScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SummariesScreen(state: SummariesState, onAction: (SummariesAction) -> Unit) {
    val configuration = LocalConfiguration.current
    val width = if (configuration.isPortrait()) {
        (configuration.screenWidthDp / 2).dp - 16.dp
    } else {
        (configuration.screenWidthDp / 4).dp - 16.dp
    }

    PullToRefresh(
        onRefresh = { onAction(SummariesAction.OnRefreshSummaries) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .edgeToEdgePadding()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(R.string.summaries_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            VerticalSpacer(of = 4.dp)
            Text(
                text = stringResource(R.string.summaries_subtitle),
                style = MaterialTheme.typography.bodyLarge,
            )
            AnimatedVisibility(visible = state.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() },
                )
            }

            if (state.healthConnectSummary != null) {
                VerticalSpacer(of = 20.dp)
                Text(
                    text = stringResource(R.string.health_connect_summary),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                VerticalSpacer(of = 8.dp)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.ic_steps,
                        value = "${state.healthConnectSummary.stepsCount}",
                    )
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.svg_calories,
                        value = "${state.healthConnectSummary.caloriesCount}",
                    )

                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.svg_sleep,
                        value = stringResource(
                            R.string.sleep_hrs,
                            state.healthConnectSummary.sleepDurationInHours,
                        ),
                    )
                }
            } else {
                VerticalSpacer(of = 20.dp)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.health_connect_no_summary),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }

            if (state.samsungHealthSummary != null) {
                VerticalSpacer(of = 20.dp)
                Text(
                    text = stringResource(R.string.samsung_health_summary),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                VerticalSpacer(of = 8.dp)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.ic_steps,
                        value = "${state.samsungHealthSummary.stepsCount}",
                    )
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.svg_calories,
                        value = "${state.samsungHealthSummary.caloriesCount}",
                    )
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.svg_sleep,
                        value = stringResource(
                            R.string.sleep_hrs,
                            state.samsungHealthSummary.sleepDurationInHours,
                        ),
                    )
                }
            } else {
                VerticalSpacer(of = 20.dp)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.samsung_health_no_summary),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }

            if (state.androidSummary != null) {
                VerticalSpacer(of = 20.dp)
                Text(
                    text = stringResource(R.string.android_summary),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                VerticalSpacer(of = 8.dp)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SummaryCard(
                        modifier = Modifier
                            .width(width)
                            .padding(8.dp),
                        iconRes = R.drawable.ic_steps,
                        value = "${state.androidSummary.stepsCount}",
                    )
                }
            } else {
                VerticalSpacer(of = 20.dp)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.android_no_summary),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SummariesPreview() {
    RookNativeTheme {
        Surface {
            SummariesScreen(
                state = SummariesState(
                    loading = false,
                    samsungHealthSummary = Summary(
                        stepsCount = 412,
                        caloriesCount = 222,
                        sleepDurationInHours = 10.40,
                    ),
                ),
                onAction = {},
            )
        }
    }
}
