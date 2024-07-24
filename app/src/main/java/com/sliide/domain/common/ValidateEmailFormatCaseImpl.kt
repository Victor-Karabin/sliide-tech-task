package com.sliide.domain.common

import java.util.regex.Pattern
import javax.inject.Inject

class ValidateEmailFormatCaseImpl @Inject constructor() : ValidateEmailFormatCase {

    override fun invoke(email: String): Result<Unit> {
        return when {
            email.isBlank() -> Result.failure(IllegalArgumentException("email is empty"))
            !EMAIL_ADDRESS.matcher(email).matches() -> {
                Result.failure(IllegalArgumentException("invalid email format"))
            }

            else -> Result.success(Unit)
        }
    }

    companion object {
        // see android.util.EMAIL_ADDRESS. The domain layer must not contains android dependencies
        // It doesn't match 'admin@mailserver1' and match 'two..dots@domain.com'
        // Backend allows 'admin@mailserver1' and prohibits 'two..dots@domain.com'
        // Backend and android client should use the same pattern or standard
        private val EMAIL_ADDRESS: Pattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}
