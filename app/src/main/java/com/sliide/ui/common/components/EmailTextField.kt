package com.sliide.ui.common.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.sliide.R

@Composable
internal fun EmailTextField(
    email: String,
    error: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null, // null is expected for this icon
                tint = MaterialTheme.colorScheme.outline
            )
        },
        placeholder = { PlaceholderText(text = stringResource(id = R.string.email)) },
        supportingText = { ErrorText(text = error) },
        value = email,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = onValueChanged,
        isError = error.isNotEmpty()
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmailTextFieldPlaceholder() {
    EmailTextField(
        email = "",
        error = "",
        onValueChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmailTextField() {
    EmailTextField(
        email = "harry.potter@gmail.com",
        error = "",
        onValueChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmailTextFieldError() {
    EmailTextField(
        email = "harry.potter",
        error = "Invalid email format",
        onValueChanged = {}
    )
}
