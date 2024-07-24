package com.sliide.ui.users.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sliide.R
import com.sliide.ui.common.components.EmailTextField
import com.sliide.ui.common.components.NameTextField
import com.sliide.ui.users.models.EmailError
import com.sliide.ui.users.models.NameError
import com.sliide.ui.users.toText

@Composable
internal fun CreateUserDialog(
    name: String,
    email: String,
    nameError: NameError,
    emailError: EmailError,
    onDismissRequest: () -> Unit,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onCreateClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(shape = AlertDialogDefaults.shape)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            NameTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                name = name,
                error = nameError.toText(),
                onValueChanged = onNameChanged
            )

            Spacer(modifier = Modifier.height(4.dp))

            EmailTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onCreateClick() }),
                email = email,
                error = emailError.toText(),
                onValueChanged = onEmailChanged
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onDismissRequest) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }

                Spacer(modifier = Modifier.width(4.dp))

                Button(onClick = onCreateClick) {
                    Text(text = stringResource(id = R.string.create))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateUserDialogEmpty() {
    CreateUserDialog(
        name = "",
        email = "",
        nameError = NameError.None,
        emailError = EmailError.None,
        onDismissRequest = {},
        onNameChanged = {},
        onEmailChanged = {},
        onCreateClick = {}
    )
}

@Preview
@Composable
private fun PreviewCreateUserDialog() {
    CreateUserDialog(
        name = "Harry Potter",
        email = "harry.potter@gmail.com",
        nameError = NameError.None,
        emailError = EmailError.None,
        onDismissRequest = {},
        onNameChanged = {},
        onEmailChanged = {},
        onCreateClick = {}
    )
}

@Preview
@Composable
private fun PreviewCreateUserDialogFieldsRequired() {
    CreateUserDialog(
        name = "",
        email = "",
        nameError = NameError.NameRequired,
        emailError = EmailError.EmailRequired,
        onDismissRequest = {},
        onNameChanged = {},
        onEmailChanged = {},
        onCreateClick = {}
    )
}

@Preview
@Composable
private fun PreviewCreateUserDialogEmailFormat() {
    CreateUserDialog(
        name = "Harry Potter",
        email = "harry.potter",
        nameError = NameError.None,
        emailError = EmailError.EmailFormat,
        onDismissRequest = {},
        onNameChanged = {},
        onEmailChanged = {},
        onCreateClick = {}
    )
}

@Preview
@Composable
private fun PreviewCreateUserDialogEmailExist() {
    CreateUserDialog(
        name = "Harry Potter",
        email = "harry.potter@gmail.com",
        nameError = NameError.None,
        emailError = EmailError.EmailExists,
        onDismissRequest = {},
        onNameChanged = {},
        onEmailChanged = {},
        onCreateClick = {}
    )
}
