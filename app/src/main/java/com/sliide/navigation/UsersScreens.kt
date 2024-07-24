package com.sliide.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sliide.ui.users.UserListScreen
import com.sliide.ui.users.UserListViewModel

internal enum class UsersScreen {
    List
}

internal fun UsersScreen.toPath(): String {
    return "users/" + when (this) {
        UsersScreen.List -> "list"
    }
}

internal fun UsersScreen.toRoute(): String {
    return this.toPath() + when (this) {
        UsersScreen.List -> ""
    }
}

internal fun NavGraphBuilder.usersNavGraph(navController: NavController) {
    composable(route = UsersScreen.List.toRoute()) {
        val viewModel = hiltViewModel<UserListViewModel>()

        UserListScreen(
            viewModel = viewModel,
            close = { navController.popBackStack() }
        )
    }
}