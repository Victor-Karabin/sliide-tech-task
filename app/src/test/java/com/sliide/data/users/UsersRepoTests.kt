package com.sliide.data.users

import com.sliide.common.MainCoroutineRule
import com.sliide.domain.users.models.CreateUserError
import com.sliide.domain.users.models.CreateUserThrowable
import com.sliide.domain.users.models.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepoTests {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val dto = listOf(
        UserDto(id = 1L, name = "Harry", email = "harry@gmail", gender = "male", status = "active"),
        UserDto(id = 2L, name = "Dobby", email = "dobby@mail", gender = "male", status = "active"),
        UserDto(id = 2L, name = "Hermione", email = "h@mail", gender = "female", status = "active")
    )

    @Test
    fun `lastUsers should use a page params from headers`() = runTest {
        val pages = 200
        val perPage = 3

        val api = mock<UsersApi> {
            val pageHeaders = Headers.Builder()
                .add(Backend.LIMIT_HEADER, perPage.toString())
                .add(Backend.PAGES_HEADER, pages.toString())
                .build()

            onBlocking { this.listUsers() }
                .doReturn(Response.success(dto, pageHeaders))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        repo.lastUsers()
        advanceUntilIdle()

        verify(api, times(1)).listUsers(pages, perPage)
    }

    @Test
    fun `createUser should use default values for gender and status`() = runTest {
        val api = mock<UsersApi>()

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val id = Long.MIN_VALUE
        val name = "Harry"
        val email = "harry@gmail.com"
        val user = User(id = id, name = name, email = email)

        repo.createUser(user)
        advanceUntilIdle()

        val dto = UserDto(id, name, email, Backend.DEF_GENDER, Backend.DEF_STATUS)
        verify(api, times(1)).create(dto)
    }

    @Test
    fun `createUser should return a EmailExists when email has been taken`() = runTest {
        val body = "[{\"field\":\"${Backend.EMAIL}\",\"message\":\"${Backend.ALREADY_TAKEN}\"}]"

        val api = mock<UsersApi> {
            onBlocking { this.create(any()) }
                .doReturn(Response.error(422, body.toResponseBody()))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val user = User(id = Long.MIN_VALUE, name = "Harry", email = "harry@gmail.com")
        val result = repo.createUser(user)
        advanceUntilIdle()

        val errors = (result.exceptionOrNull() as CreateUserThrowable).errors

        assertEquals(setOf(CreateUserError.EmailExists), errors)
    }

    @Test
    fun `createUser should return a NameRequired, EmailRequired when fields are blank`() = runTest {
        val errorBody = StringBuilder()
            .append("[")
            .append("{\"field\":\"${Backend.NAME}\",\"message\":\"${Backend.CAN_NOT_BE_BLANK}\"},")
            .append("{\"field\":\"${Backend.EMAIL}\",\"message\":\"${Backend.CAN_NOT_BE_BLANK}\"}")
            .append("]")
            .toString()

        val api = mock<UsersApi> {
            onBlocking { this.create(any()) }
                .doReturn(Response.error(422, errorBody.toResponseBody()))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val user = User(id = Long.MIN_VALUE, name = "", email = "")
        val result = repo.createUser(user)
        advanceUntilIdle()

        val errors = (result.exceptionOrNull() as CreateUserThrowable).errors

        assertEquals(setOf(CreateUserError.NameRequired, CreateUserError.EmailRequired), errors)
    }

    @Test
    fun `createUser should return a EmailInvalid when email format is wrong`() = runTest {
        val errorBody = "[{\"field\":\"${Backend.EMAIL}\",\"message\":\"${Backend.IS_INVALID}\"}]"

        val api = mock<UsersApi> {
            onBlocking { this.create(any()) }
                .doReturn(Response.error(422, errorBody.toResponseBody()))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val user = User(id = Long.MIN_VALUE, name = "", email = "harry.")
        val result = repo.createUser(user)
        advanceUntilIdle()

        val errors = (result.exceptionOrNull() as CreateUserThrowable).errors

        assertEquals(setOf(CreateUserError.EmailInvalid), errors)
    }

    @Test
    fun `createUser should return a NameUnknown when not supported name error`() = runTest {
        val errorBody = "[{\"field\":\"${Backend.NAME}\",\"message\":\"too short\"}]"

        val api = mock<UsersApi> {
            onBlocking { this.create(any()) }
                .doReturn(Response.error(422, errorBody.toResponseBody()))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val user = User(id = Long.MIN_VALUE, name = "H", email = "harry@gmail")
        val result = repo.createUser(user)
        advanceUntilIdle()

        val errors = (result.exceptionOrNull() as CreateUserThrowable).errors

        assertEquals(setOf(CreateUserError.NameUnknown), errors)
    }

    @Test
    fun `createUser should return a Unknown when not supported field`() = runTest {
        val errorBody = "[{\"field\":\"address\",\"message\":\"${Backend.CAN_NOT_BE_BLANK}\"}]"

        val api = mock<UsersApi> {
            onBlocking { this.create(any()) }
                .doReturn(Response.error(422, errorBody.toResponseBody()))
        }

        val repo = UsersRepoImpl(
            api = api,
            io = mainCoroutineRule.dispatcher
        )

        val user = User(id = Long.MIN_VALUE, name = "H", email = "harry@gmail")
        val result = repo.createUser(user)
        advanceUntilIdle()

        val errors = (result.exceptionOrNull() as CreateUserThrowable).errors

        assertEquals(setOf(CreateUserError.Unknown), errors)
    }
}