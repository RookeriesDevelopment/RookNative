package io.tryrook.rooknative.core.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.theme.RookNativeTheme

@Composable
fun NextButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FilledIconButton(
        modifier = modifier.size(60.dp),
        shape = MaterialTheme.shapes.large,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
        ),
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = stringResource(R.string.next),
                tint = MaterialTheme.colorScheme.primaryContainer,
            )
        },
    )
}

@Preview
@Composable
private fun NextButtonPreview() {
    RookNativeTheme {
        Surface {
            NextButton(onClick = {})
        }
    }
}
