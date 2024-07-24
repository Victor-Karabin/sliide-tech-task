package com.sliide.ui.users.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class UserListState {

    @Immutable
    data object Loading : UserListState()

    @Stable
    data class Items(val items: ImmutableList<UserItem>) : UserListState()

    @Stable
    data class Error(val message: String = "") : UserListState()
}