package com.sliide.ui.common.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.sliide.R

@Composable
internal fun NameTextField(
    name: String,
    error: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = null, // null is expected for this icon
                tint = MaterialTheme.colorScheme.outline
            )
        },
        placeholder = { PlaceholderText(text = stringResource(id = R.string.name)) },
        supportingText = { ErrorText(text = error) },
        value = name,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = onValueChanged,
        isError = error.isNotEmpty()
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNameTextFieldPlaceholder() {
    NameTextField(
        name = "",
        error = "",
        onValueChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNameTextField() {
    NameTextField(
        name = "Harry Potter",
        error = "",
        onValueChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNameTextFieldError() {
    NameTextField(
        name = "",
        error = "Field is required",
        onValueChanged = {}
    )
}
