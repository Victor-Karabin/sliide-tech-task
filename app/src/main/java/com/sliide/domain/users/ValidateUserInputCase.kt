package com.sliide.domain.users

import com.sliide.domain.users.models.CreateUserError

interface ValidateUserInputCase {

    operator fun invoke(name: String, email: String): Result<Set<CreateUserError>>
}
