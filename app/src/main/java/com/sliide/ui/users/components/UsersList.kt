package com.sliide.ui.users.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sliide.ui.users.models.UserItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UsersList(
    items: ImmutableList<UserItem>,
    onLongClick: (item: UserItem) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(
            count = items.size,
            key = { index: Int -> items[index].id }
        ) { index: Int ->
            val item = items[index]

            UserItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                item = item,
                onLongClick = { onLongClick(item) }
            )

            if (index < items.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewUsersList() {
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

    UsersList(
        modifier = Modifier.fillMaxWidth(),
        items = items,
        onLongClick = {}
    )
}