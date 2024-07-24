package com.sliide.data.users

import com.sliide.domain.users.models.CreateUserError
import com.sliide.domain.users.models.User

// I agree. It looks weird. But I don't want to show raw errors from backend in UI. Because:
// 1.we depend on backend implementation. Backend can send sensitive data
// 2.problems with localization
// 3.various error messages for the same cases (mobile client has validation for user input, etc.)
internal fun FieldErrorDto.toCreateUserError(): CreateUserError {
    return when (this.field) {
        Backend.EMAIL -> when (this.message) {
            Backend.IS_INVALID -> CreateUserError.EmailInvalid
            Backend.CAN_NOT_BE_BLANK -> CreateUserError.EmailRequired
            Backend.ALREADY_TAKEN -> CreateUserError.EmailExists
            else -> CreateUserError.EmailUnknown
        }

        Backend.NAME -> when (this.message) {
            Backend.CAN_NOT_BE_BLANK -> CreateUserError.NameRequired
            else -> CreateUserError.NameUnknown
        }

        else -> CreateUserError.Unknown
    }
}

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
