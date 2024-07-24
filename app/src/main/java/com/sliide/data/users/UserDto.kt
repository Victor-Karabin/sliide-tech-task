package com.sliide.data.users

internal data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    val gender: String,
    val status: String
)
