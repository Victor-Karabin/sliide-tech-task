package com.sliide.ui.users.models

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class UserListDialogs {
    data class DeleteUser(val userId: Long) : UserListDialogs()

    data object CreateUser : UserListDialogs()

    data object None : UserListDialogs()
}