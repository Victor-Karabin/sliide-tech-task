package com.sliide.ui.users

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sliide.BuildConfig
import com.sliide.R
import com.sliide.ui.SingleEventEffect
import com.sliide.ui.extensions.isScrollingUp
import com.sliide.ui.users.components.CreateUserDialog
import com.sliide.ui.users.components.DeleteUserDialog
import com.sliide.ui.users.components.EmptyPlaceholder
import com.sliide.ui.users.components.ErrorPlaceholder
import com.sliide.ui.users.components.UsersList
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.models.UserListDialogs
import com.sliide.ui.users.models.UserListState
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UserListScreen(
    viewModel: UserListViewModel, close: () -> Unit, modifier: Modifier = Modifier
) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) { viewModel.refreshUsers() }

    val context = LocalContext.current
    val errorMessage = stringResource(id = R.string.common_error_occurred)

    SingleEventEffect(sideEffectFlow = viewModel.error) { throwable ->
        val message = if (BuildConfig.DEBUG) throwable.localizedMessage else errorMessage
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        close()
    }

    val dialog by viewModel.dialogs.collectAsStateWithLifecycle()

    when (val type = dialog) {
        UserListDialogs.CreateUser -> CreateUserDialog(
            onDismissRequest = viewModel::hideDialog,
            onConfirmClick = viewModel::onCreateClick
        )

        is UserListDialogs.DeleteUser -> DeleteUserDialog(onDismissRequest = viewModel::hideDialog,
            onConfirmClick = { viewModel.onDeleteClick(type.userId) })

        UserListDialogs.None -> Unit
    }

    val state by viewModel.screenState.collectAsStateWithLifecycle()

    UserListScreen(
        modifier = modifier,
        state = state,
        onLongClick = viewModel::onLongClick,
        onFabClick = viewModel::onFabClick,
        onFetchClick = viewModel::refreshUsers
    )
}

@Composable
private fun UserListScreen(
    state: UserListState,
    onLongClick: (UserItem) -> Unit,
    onFabClick: () -> Unit,
    onFetchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrollingUp by listState.isScrollingUp()

    Scaffold(
        modifier = modifier, floatingActionButton = {
            AnimatedVisibility(
                visible = isScrollingUp && state is UserListState.Items,
                enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight * 2 }),
                exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight * 2 })
            ) {
                FloatingActionButton(
                    onClick = onFabClick,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_user),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding: PaddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            when (state) {
                is UserListState.Error -> ErrorPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    message = state.message
                )

                is UserListState.Items -> {
                    if (state.items.isEmpty()) {
                        EmptyPlaceholder(
                            modifier = Modifier.fillMaxSize(),
                            onFetchClick = onFetchClick
                        )
                    } else {
                        UsersList(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            items = state.items,
                            onLongClick = onLongClick
                        )
                    }
                }

                UserListState.Loading -> CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewUserListScreenLoading() {
    UserListScreen(state = UserListState.Loading,
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {})
}

@Preview
@Composable
private fun PreviewUserListScreenItems() {
    val items = persistentListOf(
        UserItem(id = 1L, name = "Harry Potter", "harry.potter@gmail.com", "30s ago"),
        UserItem(id = 2L, name = "Hermione Granger", "hermione.granger@yahoo.com", ""),
        UserItem(id = 3L, name = "Ron Weasley", "ronwh@aol.com", "1m ago"),
        UserItem(id = 4L, name = "Tom Riddle", "tom.marvolo.riddle@outlook.com", "1h 32m ago"),
        UserItem(
            id = 5L,
            name = "Albus Percival Wulfric Brian Dumbledore",
            "albus.percival.wulfric.brian.dumbledore@protonmail.com",
            "1d ago"
        )
    )

    UserListScreen(
        state = UserListState.Items(items),
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {}
    )
}

@Preview
@Composable
private fun PreviewUserListScreenNoItems() {
    UserListScreen(
        state = UserListState.Items(persistentListOf()),
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {}
    )
}

@Preview
@Composable
private fun PreviewUserListScreenError() {
    UserListScreen(
        state = UserListState.Error(stringResource(R.string.common_error_occurred)),
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {}
    )
}