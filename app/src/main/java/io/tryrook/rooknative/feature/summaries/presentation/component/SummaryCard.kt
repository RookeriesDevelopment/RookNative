package io.tryrook.rooknative.feature.summaries.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@Composable
fun SummaryCard(modifier: Modifier = Modifier, @DrawableRes iconRes: Int, value: String) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
            VerticalSpacer(of = 8.dp)
            Text(text = value)
        }
    }
}

@Preview
@Composable
private fun SummaryCardPreview() {
    RookNativeTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                SummaryCard(iconRes = R.drawable.svg_sleep, value = "8 hrs")
            }
        }
    }
}
