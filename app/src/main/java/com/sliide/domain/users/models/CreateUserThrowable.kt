package com.sliide.domain.users.models

class CreateUserThrowable(val errors: Set<CreateUserError>) : Throwable()
