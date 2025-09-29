@file:OptIn(ExperimentalLayoutApi::class)

package io.tryrook.rooknative.feature.summaries.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.extension.isPortrait
import io.tryrook.rooknative.core.presentation.modifier.edgeToEdgePadding
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme
import io.tryrook.rooknative.feature.summaries.presentation.component.SummaryCard

class SummariesScreenDestination : Screen {
    @Composable
    override fun Content() {
        SummariesScreen()
    }
}

@Composable
fun SummariesScreen() {
    val configuration = LocalConfiguration.current

    val gridCells = remember(configuration) {
        if (configuration.isPortrait()) {
            GridCells.Fixed(2)
        } else {
            GridCells.Fixed(3)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .edgeToEdgePadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
        VerticalSpacer(of = 20.dp)
        LazyVerticalGrid(gridCells) {
            item {
                SummaryCard(
                    modifier = Modifier.padding(8.dp),
                    iconRes = R.drawable.ic_steps,
                    value = "2347",
                )
            }
            item {
                SummaryCard(
                    modifier = Modifier.padding(8.dp),
                    iconRes = R.drawable.svg_calories,
                    value = "2347",
                )
            }
            item {
                SummaryCard(
                    modifier = Modifier.padding(8.dp),
                    iconRes = R.drawable.svg_sleep,
                    value = "8 hrs",
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
            SummariesScreen()
        }
    }
}
