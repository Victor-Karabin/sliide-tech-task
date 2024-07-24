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
import com.sliide.R
import com.sliide.ui.common.SingleEventEffect
import com.sliide.ui.common.components.ErrorPlaceholder
import com.sliide.ui.extensions.isScrollingUp
import com.sliide.ui.users.components.CreateUserDialog
import com.sliide.ui.users.components.DeleteUserDialog
import com.sliide.ui.users.components.EmptyUsersListPlaceholder
import com.sliide.ui.users.components.UsersList
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.models.UserListDialog
import com.sliide.ui.users.models.UserListState
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
internal fun UserListScreen(
    viewModel: UserListViewModel,
    close: () -> Unit,
    modifier: Modifier = Modifier
) {
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.fetchUsers()
        viewModel.startRefreshingUsers()
    }

    val context = LocalContext.current
    val unknownError = stringResource(id = R.string.common_error_occurred)

    SingleEventEffect(sideEffectFlow = viewModel.unknownError) {
        Toast.makeText(context, unknownError, Toast.LENGTH_LONG).show()
    }

    val dialog by viewModel.dialogs.collectAsStateWithLifecycle()

    when (val type = dialog) {
        is UserListDialog.CreateUser -> CreateUserDialog(
            name = type.name,
            email = type.email,
            nameError = type.nameError,
            emailError = type.emailError,
            onDismissRequest = viewModel::hideDialog,
            onNameChanged = viewModel::onNameChanged,
            onEmailChanged = viewModel::onEmailChanged,
            onCreateClick = viewModel::onCreateClick
        )

        is UserListDialog.DeleteUser -> DeleteUserDialog(
            onDismissRequest = viewModel::hideDialog,
            onConfirmClick = { viewModel.onDeleteClick(type.userId) }
        )

        UserListDialog.None -> Unit
    }

    val state by viewModel.screenState.collectAsStateWithLifecycle()

    UserListScreen(
        modifier = modifier,
        state = state,
        onCloseClick = close,
        onLongClick = viewModel::onLongClick,
        onFabClick = viewModel::onFabClick,
        onFetchClick = viewModel::fetchUsers
    )
}

@Composable
private fun UserListScreen(
    state: UserListState,
    onCloseClick: () -> Unit,
    onLongClick: (UserItem) -> Unit,
    onFabClick: () -> Unit,
    onFetchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrollingUp by listState.isScrollingUp()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
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
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is UserListState.Error -> ErrorPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    message = state.message,
                    onCloseClick = onCloseClick
                )

                is UserListState.Items -> {
                    if (state.items.isEmpty()) {
                        EmptyUsersListPlaceholder(
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
    UserListScreen(
        state = UserListState.Loading,
        onCloseClick = {},
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {}
    )
}

@Preview
@Composable
private fun PreviewUserListScreenItems() {
    val items = persistentListOf(
        UserItem(
            id = 1L,
            name = "Harry Potter",
            email = "harry.potter@gmail.com",
            exists = 22.toDuration(DurationUnit.SECONDS),
            createdAt = Long.MIN_VALUE
        ),
        UserItem(
            id = 2L,
            name = "Hermione Granger",
            email = "hermione.granger@yahoo.com",
            exists = 2.toDuration(DurationUnit.MINUTES),
            createdAt = Long.MIN_VALUE
        ),
        UserItem(
            id = 3L,
            name = "Ron Weasley",
            email = "ronwh@aol.com",
            exists = 10.toDuration(DurationUnit.MINUTES),
            createdAt = Long.MIN_VALUE
        ),
        UserItem(
            id = 4L,
            name = "Tom Riddle",
            email = "tom.marvolo.riddle@outlook.com",
            exists = 12.toDuration(DurationUnit.HOURS),
            createdAt = Long.MIN_VALUE
        ),
        UserItem(
            id = 5L,
            name = "Albus Percival Wulfric Brian Dumbledore",
            email = "albus.percival.wulfric.brian.dumbledore@protonmail.com",
            exists = 3.toDuration(DurationUnit.DAYS),
            createdAt = Long.MIN_VALUE
        )
    )

    UserListScreen(
        state = UserListState.Items(items),
        onCloseClick = {},
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
        onCloseClick = {},
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
        onCloseClick = {},
        onLongClick = {},
        onFabClick = {},
        onFetchClick = {}
    )
}
