package com.sliide.domain.users

import com.sliide.domain.common.ValidateEmailFormatCase
import com.sliide.domain.users.models.CreateUserError
import javax.inject.Inject

internal class ValidateUserInputCaseImpl @Inject constructor(
    private val validateEmail: ValidateEmailFormatCase
) : ValidateUserInputCase {

    override fun invoke(name: String, email: String): Result<Set<CreateUserError>> {
        val results = HashSet<CreateUserError>()
        if (name.isBlank()) results.add(CreateUserError.NameRequired)

        when {
            email.isBlank() -> results.add(CreateUserError.EmailRequired)
            validateEmail(email).isFailure -> results.add(CreateUserError.EmailInvalid)
        }

        return Result.success(results)
    }
}
