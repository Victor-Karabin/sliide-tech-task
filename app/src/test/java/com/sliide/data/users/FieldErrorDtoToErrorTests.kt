package com.sliide.data.users

import com.sliide.domain.users.models.CreateUserError
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class FieldErrorDtoToErrorTests(
    private val dto: FieldErrorDto,
    private val expected: CreateUserError
) {
    @Test
    fun `given error when mapping then expected result`() {
        val result = dto.toCreateUserError()
        assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "#{index}: {0} expected: {1}")
        fun data(): Collection<Array<Any>> {
            val email = Backend.EMAIL
            val name = Backend.NAME
            val invalid = Backend.IS_INVALID
            val blank = Backend.CAN_NOT_BE_BLANK
            val exists = Backend.ALREADY_TAKEN

            return listOf(
                arrayOf(FieldErrorDto(email, invalid), CreateUserError.EmailInvalid),
                arrayOf(FieldErrorDto(email, blank), CreateUserError.EmailRequired),
                arrayOf(FieldErrorDto(email, exists), CreateUserError.EmailExists),
                arrayOf(FieldErrorDto(email, "custom"), CreateUserError.EmailUnknown),
                arrayOf(FieldErrorDto(name, blank), CreateUserError.NameRequired),
                arrayOf(FieldErrorDto(name, "custom"), CreateUserError.NameUnknown),
                arrayOf(FieldErrorDto("custom", invalid), CreateUserError.Unknown),
                arrayOf(FieldErrorDto("", blank), CreateUserError.Unknown),
                arrayOf(FieldErrorDto("custom", exists), CreateUserError.Unknown)
            )
        }
    }
}