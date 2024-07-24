package com.sliide.domain.users.models

enum class CreateUserError {
    NameRequired,
    EmailRequired,
    EmailExists,
    EmailInvalid
}
