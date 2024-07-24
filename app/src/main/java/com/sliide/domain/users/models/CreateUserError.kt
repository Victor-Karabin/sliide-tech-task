package com.sliide.domain.users.models

enum class CreateUserError {
    NameRequired,
    NameUnknown,
    EmailRequired,
    EmailExists,
    EmailInvalid,
    EmailUnknown,
    Unknown
}
