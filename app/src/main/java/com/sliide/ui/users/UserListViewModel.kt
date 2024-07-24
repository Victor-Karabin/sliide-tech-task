package com.sliide.ui.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.common.DateTime
import com.sliide.common.flatMap
import com.sliide.data.users.UsersRepo
import com.sliide.domain.users.ValidateUserInputCase
import com.sliide.domain.users.models.CreateUserError
import com.sliide.domain.users.models.CreateUserError.EmailExists
import com.sliide.domain.users.models.CreateUserError.EmailInvalid
import com.sliide.domain.users.models.CreateUserError.EmailRequired
import com.sliide.domain.users.models.CreateUserError.EmailUnknown
import com.sliide.domain.users.models.CreateUserError.NameRequired
import com.sliide.domain.users.models.CreateUserError.NameUnknown
import com.sliide.domain.users.models.CreateUserError.Unknown
import com.sliide.domain.users.models.CreateUserThrowable
import com.sliide.domain.users.models.User
import com.sliide.ui.users.models.EmailError
import com.sliide.ui.users.models.NameError
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.models.UserListDialog
import com.sliide.ui.users.models.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val usersRepo: UsersRepo,
    private val validateInputCase: ValidateUserInputCase,
    private val dateTime: DateTime
) : ViewModel() {

    private val mutableScreenState = MutableStateFlow<UserListState>(UserListState.Loading)
    internal val screenState = mutableScreenState.asStateFlow()

    private val mutableDialogs = MutableStateFlow<UserListDialog>(UserListDialog.None)
    internal val dialogs = mutableDialogs.asStateFlow()

    private val unknownErrorChannel = Channel<Throwable>(capacity = Channel.BUFFERED)
    internal val unknownError: Flow<Throwable>
        get() = unknownErrorChannel.receiveAsFlow()

    internal fun fetchUsers() {
        viewModelScope.launch {
            mutableScreenState.value = UserListState.Loading

            usersRepo.lastUsers()
                .onSuccess { users ->
                    val createdAt = dateTime.currentTimestamp()
                    val items = users.map { user -> user.toItem(Duration.ZERO, createdAt) }
                        .toImmutableList()
                    mutableScreenState.value = UserListState.Items(items)
                }
                .onFailure { throwable ->
                    Log.d(TAG, "failed refresh users", throwable)
                    mutableScreenState.value = UserListState.Error()
                }
        }
    }

    internal fun startRefreshingUsers() {
        viewModelScope.launch {
            while (viewModelScope.isActive) {
                delay(REFRESH_EXISTS_DELAY)

                val state = screenState.value
                if (state is UserListState.Items) {
                    val now = dateTime.currentTimestamp()
                    val refreshed = state.items.map { item ->
                        val exists = (now - item.createdAt).toDuration(DurationUnit.MILLISECONDS)
                        item.copy(exists = exists)
                    }.toImmutableList()

                    mutableScreenState.value = UserListState.Items(refreshed)
                }
            }
        }
    }

    internal fun onFabClick() {
        mutableDialogs.value = UserListDialog.CreateUser.Empty
    }

    internal fun onLongClick(item: UserItem) {
        mutableDialogs.value = UserListDialog.DeleteUser(item.id)
    }

    internal fun onDeleteClick(userId: Long) {
        viewModelScope.launch {
            hideDialog()

            val state = screenState.value
            if (state is UserListState.Items) {
                val prevItems = state.items
                mutableScreenState.value = UserListState.Loading

                usersRepo.deleteUser(userId)
                    .onSuccess {
                        val items = prevItems.toMutableList()
                        val filtered = items.filter { item -> item.id != userId }
                        mutableScreenState.value = UserListState.Items(filtered.toImmutableList())
                    }
                    .onFailure { throwable: Throwable ->
                        Log.d(TAG, "failed delete user: $userId", throwable)
                        unknownErrorChannel.send(throwable)
                        mutableScreenState.value = state
                    }
            } else {
                Log.d(TAG, "invalid state. try to remove for state: $state")
            }
        }
    }

    internal fun hideDialog() {
        if (dialogs.value == UserListDialog.None) {
            Log.d(TAG, "invalid state. try to hide dialog for ${UserListDialog.None}")
        }

        mutableDialogs.value = UserListDialog.None
    }

    internal fun onNameChanged(name: String) {
        val dialog = dialogs.value
        if (dialog is UserListDialog.CreateUser) {
            mutableDialogs.value = dialog.copy(name = name, nameError = NameError.None)
        } else {
            Log.d(TAG, "invalid state. try to change name for dialog: $dialog")
        }
    }

    internal fun onEmailChanged(email: String) {
        val dialog = dialogs.value
        if (dialog is UserListDialog.CreateUser) {
            mutableDialogs.value = dialog.copy(email = email, emailError = EmailError.None)
        } else {
            Log.d(TAG, "invalid state. try to change email for dialog: $dialog")
        }
    }

    internal fun onCreateClick() {
        viewModelScope.launch {
            val state = screenState.value
            val dialog = dialogs.value

            if (state is UserListState.Items && dialog is UserListDialog.CreateUser) {
                val prevItems = state.items

                val name = dialog.name.trim()
                val email = dialog.email.trim()
                validateInputCase(name, email)
                    .flatMap { errors: Set<CreateUserError> ->
                        if (errors.isEmpty()) {
                            hideDialog()
                            mutableScreenState.value = UserListState.Loading
                            usersRepo.createUser(User(id = Long.MIN_VALUE, name, email))
                        } else {
                            Result.failure(CreateUserThrowable(errors))
                        }
                    }
                    .onSuccess { user ->
                        val items = prevItems.toMutableList()
                        items.add(0, user.toItem(Duration.ZERO, dateTime.currentTimestamp()))
                        mutableScreenState.value = UserListState.Items(items.toImmutableList())
                    }
                    .onFailure { throwable: Throwable ->
                        mutableDialogs.value = if (throwable is CreateUserThrowable) {
                            handleErrors(dialog, throwable.errors)
                        } else {
                            Log.d(TAG, "failed create user: $name, $email", throwable)
                            unknownErrorChannel.send(throwable)
                            dialog
                        }
                        mutableScreenState.value = state
                    }
            } else {
                Log.d(TAG, "invalid state. try to create for state: $state, dialog: $dialog")
            }
        }
    }

    private suspend fun handleErrors(
        dialog: UserListDialog.CreateUser,
        errors: Set<CreateUserError>
    ): UserListDialog {

        var nameError = NameError.None
        var emailError = EmailError.None

        errors.forEach { error ->
            when (error) {
                NameRequired -> nameError = NameError.NameRequired
                EmailRequired -> emailError = EmailError.EmailRequired
                EmailExists -> emailError = EmailError.EmailExists
                EmailInvalid -> emailError = EmailError.EmailFormat
                NameUnknown,
                EmailUnknown,
                Unknown -> unknownErrorChannel.send(IllegalArgumentException())
            }
        }

        return dialog.copy(nameError = nameError, emailError = emailError)
    }

    private companion object {
        private const val TAG = "USERS"
        private const val REFRESH_EXISTS_DELAY = 60_000L // 1 min
    }
}
