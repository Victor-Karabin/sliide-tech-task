package com.sliide.ui.users

import androidx.lifecycle.ViewModel
import com.sliide.boundary.users.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val usersRepo: UsersRepo
) : ViewModel() {
}