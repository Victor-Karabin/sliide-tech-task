package com.sliide.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sliide.R

@Composable
internal fun ErrorPlaceholder(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = ""
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(80.dp),
            imageVector = Icons.Default.Build,
            contentDescription = message,
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message.ifEmpty { stringResource(R.string.common_error_occurred) },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCloseClick) {
            Text(text = stringResource(id = R.string.close))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewErrorPlaceholderMessage() {
    ErrorPlaceholder(
        message = "Error occurred",
        onCloseClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewErrorPlaceholderDefault() {
    ErrorPlaceholder(
        onCloseClick = {}
    )
}
