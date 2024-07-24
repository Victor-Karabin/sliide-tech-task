package com.sliide.data.users

import com.sliide.data.rest.RestThrowable
import com.sliide.domain.users.models.CreateUserError
import com.sliide.domain.users.models.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private const val EMAIL = "email"
private const val NAME = "name"
private const val IS_INVALID = "is invalid"
private const val CAN_NOT_BE_BLANK = "can't be blank"
private const val ALREADY_TAKEN = "has already been taken"

// I agree. It looks weird. But I don't want to show raw errors from backend in UI. Because:
// 1.we depend on backend implementation. Backend can send sensitive data
// 2.problems with localization
// 3.various error messages for the same cases (mobile client has validation for user input, etc.)
internal fun RestThrowable.toUserErrors(moshi: Moshi): Set<CreateUserError>? {
    val body = this.body
    if (body.isNullOrBlank()) return null

    val listType = Types.newParameterizedType(Set::class.java, UserFieldErrorDto::class.java)
    val adapter = moshi.adapter<Set<UserFieldErrorDto>>(listType)
    val errors = adapter.fromJson(body)

    return errors?.map { error ->
        when (error.field) {
            EMAIL -> when (error.message) {
                IS_INVALID -> CreateUserError.EmailInvalid
                CAN_NOT_BE_BLANK -> CreateUserError.EmailRequired
                ALREADY_TAKEN -> CreateUserError.EmailExists
                else -> throw IllegalArgumentException("not supported error: $error")
            }

            NAME -> when (error.message) {
                CAN_NOT_BE_BLANK -> CreateUserError.NameRequired
                else -> throw IllegalArgumentException("not supported error: $error")
            }

            else -> throw IllegalArgumentException("not supported error: $error")
        }
    }?.toSet()
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
