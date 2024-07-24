package com.sliide.ui.users.models

import androidx.compose.runtime.Stable
import kotlin.time.Duration

@Stable
internal data class UserItem(
    val id: Long,
    val name: String,
    val email: String,
    val exists: Duration, // not stable, but it's ok. after updating ui is notified
    val createdAt: Long
)