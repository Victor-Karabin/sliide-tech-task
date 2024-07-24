package com.sliide.ui.users.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
internal sealed class UserListDialog {

    @Stable
    data class DeleteUser(val userId: Long) : UserListDialog()

    @Stable
    data class CreateUser(
        val name: String,
        val nameError: NameError,
        val email: String,
        val emailError: EmailError
    ) : UserListDialog() {
        companion object {
            val Empty = CreateUser("", NameError.None, "", EmailError.None)
        }
    }

    @Immutable
    data object None : UserListDialog()
}