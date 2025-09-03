package io.tryrook.rooknative.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tryrook.rooknative.R
import io.tryrook.rooknative.core.presentation.component.HorizontalExpandedSpacer
import io.tryrook.rooknative.core.presentation.component.VerticalSpacer
import io.tryrook.rooknative.ui.theme.RookNativeTheme

@Composable
fun LoginScreenRoot(toHome: () -> Unit) {
    LoginScreen(toHome = toHome)
}

@Composable
private fun LoginScreen(toHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(96.dp),
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )

        VerticalSpacer(of = 12.dp)

        Text(
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.sign_in_to_continue),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )

        VerticalSpacer(of = 20.dp)

        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            value = "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.email)) }
        )
        Row {
            HorizontalExpandedSpacer()
            TextButton(
                onClick = {},
                content = { Text(text = stringResource(R.string.forget_password)) }
            )
        }

        VerticalSpacer(of = 20.dp)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = toHome,
            content = { Text(text = stringResource(R.string.sign_in)) }
        )
    }
}

@Preview
@Composable
private fun LoginPreview() {
    RookNativeTheme {
        Surface {
            LoginScreen(
                toHome = {}
            )
        }
    }
}
