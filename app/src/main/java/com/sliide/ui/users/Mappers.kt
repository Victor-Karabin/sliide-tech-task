package com.sliide.ui.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.sliide.R
import com.sliide.domain.users.models.User
import com.sliide.ui.users.models.EmailError
import com.sliide.ui.users.models.NameError
import com.sliide.ui.users.models.UserItem

internal fun User.toItem(created: String = ""): UserItem {
    return UserItem(
        id = this.id,
        name = this.name,
        email = this.email,
        created = created
    )
}

@Composable
@ReadOnlyComposable
internal fun NameError.toText(): String {
    return when (this) {
        NameError.NameRequired -> stringResource(id = R.string.field_required)
        NameError.None -> ""
    }
}

@Composable
@ReadOnlyComposable
internal fun EmailError.toText(): String {
    return when (this) {
        EmailError.EmailRequired -> stringResource(id = R.string.field_required)
        EmailError.EmailFormat -> stringResource(id = R.string.email_invalid_format)
        EmailError.EmailExists -> stringResource(id = R.string.email_has_been_taken)
        EmailError.None -> ""
    }
}