package com.sliide.domain.users

import com.sliide.domain.common.ValidateEmailFormatCase
import com.sliide.domain.users.models.CreateUserError
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import kotlin.properties.Delegates.notNull

@RunWith(Parameterized::class)
internal class ValidateUserInputCaseTests(
    private val name: String,
    private val email: String,
    private val isCorrectEmail: Boolean,
    private val expected: Set<CreateUserError>
) {
    private var usecase by notNull<ValidateUserInputCase>()

    @Before
    fun before() {
        val positive = Result.success(Unit)
        val negative = Result.failure<Unit>(IllegalStateException())

        val repo = mock(ValidateEmailFormatCase::class.java)
            .stub {
                on { this.invoke(email) }
                    .doReturn(if (isCorrectEmail) positive else negative)
            }

        usecase = ValidateUserInputCaseImpl(repo)
    }


    @Test
    fun `given user input when validating then expected result`() {
        val result = usecase(name, email)
        assertEquals(expected, result.getOrNull())
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "#{index}: {0}, {1} ({2}) expected: {3}")
        fun data(): Collection<Array<Any>> {
            val empty = setOf(CreateUserError.NameRequired, CreateUserError.EmailRequired)
            val nameEmail = setOf(CreateUserError.NameRequired, CreateUserError.EmailInvalid)

            return listOf(
                arrayOf("Harry", "simple@example.com", true, emptySet<CreateUserError>()),
                arrayOf("", "very.common@example.com", true, setOf(CreateUserError.NameRequired)),
                arrayOf("Hermione", "", false, setOf(CreateUserError.EmailRequired)),
                arrayOf("Dobby", "plainaddress", false, setOf(CreateUserError.EmailInvalid)),
                arrayOf("", "@no-local-part.com", false, nameEmail),
                arrayOf("", "", false, empty)
            )
        }
    }
}
