package com.sliide.ui.users

import com.sliide.common.DateTime
import com.sliide.common.MainCoroutineRule
import com.sliide.data.users.UsersRepo
import com.sliide.domain.users.ValidateUserInputCase
import com.sliide.domain.users.models.CreateUserError
import com.sliide.domain.users.models.CreateUserThrowable
import com.sliide.ui.users.models.EmailError
import com.sliide.ui.users.models.NameError
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.models.UserListDialog
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTests {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `empty create user dialog should be shown when fab is clicked`() = runTest {
        val usersRepo = mock<UsersRepo>()
        val dateTime = mock<DateTime>()
        val validateInputCase = mock<ValidateUserInputCase>()
        val viewModel = UserListViewModel(usersRepo, validateInputCase, dateTime)

        viewModel.onFabClick()

        assertEquals(UserListDialog.CreateUser.Empty, viewModel.dialogs.value)
    }

    @Test
    fun `delete user dialog should be shown when item is long pressed`() = runTest {
        val usersRepo = mock<UsersRepo>()
        val dateTime = mock<DateTime>()
        val validateInputCase = mock<ValidateUserInputCase>()
        val viewModel = UserListViewModel(usersRepo, validateInputCase, dateTime)

        val id = 1L
        val item = UserItem(
            id = id,
            name = "Harry",
            email = "h@gmail.com",
            exists = Duration.ZERO,
            createdAt = Long.MIN_VALUE
        )
        viewModel.onLongClick(item)

        assertEquals(UserListDialog.DeleteUser(id), viewModel.dialogs.value)
    }

    @Test
    fun `name required error should be shown when create with empty name`() = runTest {
        val usersRepo = mock<UsersRepo> {
            onBlocking { this.lastUsers() }
                .doReturn(Result.success(emptyList()))
        }
        val dateTime = mock<DateTime>()
        val validateInputCase = mock<ValidateUserInputCase> {
            on { this.invoke(any(), any()) }
                .doReturn(Result.failure(CreateUserThrowable(setOf(CreateUserError.NameRequired))))
        }
        val viewModel = UserListViewModel(usersRepo, validateInputCase, dateTime)

        viewModel.fetchUsers()
        advanceUntilIdle()

        viewModel.onFabClick()
        viewModel.onCreateClick()
        advanceUntilIdle()

        val expected = UserListDialog.CreateUser(
            name = "",
            nameError = NameError.NameRequired,
            email = "",
            emailError = EmailError.None
        )

        assertEquals(expected, viewModel.dialogs.value)
    }

    @Test
    fun `name error should be hidden when user changed name`() = runTest {
        val usersRepo = mock<UsersRepo> {
            onBlocking { this.lastUsers() }
                .doReturn(Result.success(emptyList()))
        }
        val dateTime = mock<DateTime>()
        val validateInputCase = mock<ValidateUserInputCase> {
            on { this.invoke(any(), any()) }
                .doReturn(Result.failure(CreateUserThrowable(setOf(CreateUserError.NameRequired))))
        }
        val viewModel = UserListViewModel(usersRepo, validateInputCase, dateTime)

        viewModel.fetchUsers()
        advanceUntilIdle()

        viewModel.onFabClick()
        viewModel.onCreateClick()
        advanceUntilIdle()

        val nameInput = "H"
        viewModel.onNameChanged(nameInput)

        val expected = UserListDialog.CreateUser(
            name = nameInput,
            nameError = NameError.None,
            email = "",
            emailError = EmailError.None
        )

        assertEquals(expected, viewModel.dialogs.value)
    }
}