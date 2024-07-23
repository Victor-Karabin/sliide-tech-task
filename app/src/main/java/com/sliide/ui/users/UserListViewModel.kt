package com.sliide.ui.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.BuildConfig
import com.sliide.boundary.users.User
import com.sliide.boundary.users.UsersRepo
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.models.UserListDialogs
import com.sliide.ui.users.models.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val usersRepo: UsersRepo
) : ViewModel() {

    private val mutableScreenState = MutableStateFlow<UserListState>(UserListState.Loading)
    internal val screenState = mutableScreenState.asStateFlow()

    private val mutableDialogs = MutableStateFlow<UserListDialogs>(UserListDialogs.None)
    internal val dialogs = mutableDialogs.asStateFlow()

    private val errorChannel = Channel<Throwable>(capacity = Channel.BUFFERED)
    internal val error: Flow<Throwable>
        get() = errorChannel.receiveAsFlow()

    internal fun refreshUsers() {
        viewModelScope.launch {
            mutableScreenState.value = UserListState.Loading

            usersRepo.users()
                .onSuccess { users ->
                    val items = users.map { user -> user.toItem("") }.toImmutableList()
                    mutableScreenState.value = UserListState.Items(items)
                }
                .onFailure { throwable ->
                    val message = if (BuildConfig.DEBUG) throwable.message ?: "" else ""
                    mutableScreenState.value = UserListState.Error(message)
                }
        }
    }

    internal fun onFabClick() {
        mutableDialogs.value = UserListDialogs.CreateUser
    }

    internal fun onLongClick(item: UserItem) {
        mutableDialogs.value = UserListDialogs.DeleteUser(item.id)
    }

    internal fun onDeleteClick(userId: Long) {
        viewModelScope.launch {
            hideDialog()

            val state = mutableScreenState.value
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
                        errorChannel.send(throwable)
                        mutableScreenState.value = state
                    }
            } else {
                Log.d(TAG, "invalid state. try to remove for state: $state")
            }
        }
    }

    internal fun onCreateClick(name: String, email: String) {
        viewModelScope.launch {
            hideDialog()

            val state = mutableScreenState.value
            if (state is UserListState.Items) {
                val prevItems = state.items
                mutableScreenState.value = UserListState.Loading

                val create = User(id = Long.MIN_VALUE, name, email)
                usersRepo.createUser(create)
                    .onSuccess { user ->
                        val items = prevItems.toMutableList()
                        items.add(0, user.toItem(""))
                        mutableScreenState.value = UserListState.Items(items.toImmutableList())
                    }
                    .onFailure { throwable: Throwable ->
                        errorChannel.send(throwable)
                        mutableScreenState.value = state
                    }
            } else {
                Log.d(TAG, "invalid state. try to create for state: $state")
            }
        }
    }

    internal fun hideDialog() {
        if (mutableDialogs.value == UserListDialogs.None) {
            Log.d(TAG, "invalid state. try to hide dialog for ${UserListDialogs.None}")
        }

        mutableDialogs.value = UserListDialogs.None
    }

    private companion object {
        private const val TAG = "USERS"
    }
}