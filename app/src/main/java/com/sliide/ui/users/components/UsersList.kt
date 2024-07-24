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
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

    UsersList(
        modifier = Modifier.fillMaxWidth(),
        items = items,
        onLongClick = {}
    )
}
