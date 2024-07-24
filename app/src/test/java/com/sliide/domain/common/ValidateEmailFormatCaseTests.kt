package com.sliide.domain.common

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class ValidateEmailFormatCaseTests(
    private val email: String,
    private val isCorrect: Boolean
) {
    private var usecase: ValidateEmailFormatCase = ValidateEmailFormatCaseImpl()

    @Test
    fun `given email when validating then expected result`() {
        val result = usecase(email = email)
        assertEquals(isCorrect, result.isSuccess)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "#{index}: {0} is correct: {1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                // Valid emails
                arrayOf("simple@example.com", true),
                arrayOf("very.common@example.com", true),
                arrayOf("disposable.style.email.with+symbol@example.com", true),
                arrayOf("other.email-with-dash@example.com", true),
                arrayOf("fully-qualified-domain@example.com", true),
                arrayOf("user.name+tag+sorting@example.com", true),
                arrayOf("x@example.com", true),
                arrayOf("example-indeed@strange-example.com", true),
                // arrayOf("admin@mailserver1", true),
                arrayOf("example@s.example", true),

                // Invalid emails
                arrayOf("plainaddress", false),
                arrayOf("@no-local-part.com", false),
                arrayOf("Outlook Contact <outlook-contact@domain.com>", false),
                arrayOf("no-at.domain.com", false),
                arrayOf("no-tld@domain", false),
                arrayOf(";beginning-semicolon@domain.co.uk", false),
                arrayOf("middle-semicolon@domain.co;uk", false),
                arrayOf("trailing-semicolon@domain.com;", false),
                arrayOf("missing-dot@com", false),
                // arrayOf("two..dots@domain.com", false),
                arrayOf("", false),
                arrayOf(" ", false)
            )
        }
    }
}
