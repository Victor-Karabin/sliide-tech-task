package com.sliide.data.users

import com.sliide.boundary.users.User

internal fun UserDto.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email
    )
}

internal fun User.toDto(gender: String, status: String): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        email = this.email,
        gender = gender,
        status = status
    )
}