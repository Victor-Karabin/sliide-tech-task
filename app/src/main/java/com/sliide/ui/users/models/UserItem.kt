package com.sliide.ui.users.models

import androidx.compose.runtime.Stable

@Stable
internal data class UserItem(
    val id: Long,
    val name: String,
    val email: String,
    val created: String
)