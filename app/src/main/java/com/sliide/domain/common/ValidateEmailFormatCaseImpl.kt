package com.sliide.domain.common

import android.util.Patterns
import javax.inject.Inject

class ValidateEmailFormatCaseImpl @Inject constructor() : ValidateEmailFormatCase {

    override fun invoke(email: String): Result<Unit> {
        return when {
            email.isBlank() -> Result.failure(IllegalArgumentException("email is empty"))
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Result.failure(IllegalArgumentException("invalid email format"))
            }

            else -> Result.success(Unit)
        }
    }
}
