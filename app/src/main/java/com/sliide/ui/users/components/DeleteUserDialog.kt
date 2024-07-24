package com.sliide.ui.users.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sliide.R

@Composable
internal fun DeleteUserDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Text(
                text = stringResource(id = R.string.confirm_delete_user),
                style = MaterialTheme.typography.labelLarge
            )
        },
        confirmButton = {
            Button(onClick = onConfirmClick) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewDeleteUserDialog() {
    DeleteUserDialog(
        onDismissRequest = {},
        onConfirmClick = {}
    )
}
