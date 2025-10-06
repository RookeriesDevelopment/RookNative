package io.tryrook.rooknative.core.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.MessageAndButton(
    @StringRes messageRes: Int,
    @StringRes buttonRes: Int,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(messageRes),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
    )
    VerticalSpacer(of = 12.dp)
    Button(
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        content = { Text(text = stringResource(buttonRes)) },
    )
}
