package com.sliide.ui.users.models

import androidx.compose.runtime.Immutable

@Immutable
internal enum class EmailError {
    EmailRequired,
    EmailFormat,
    EmailExists,
    None
}