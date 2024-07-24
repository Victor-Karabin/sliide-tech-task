package com.sliide.ui.users.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sliide.ui.users.models.UserItem
import com.sliide.ui.users.toCreatedAgoText
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UserItem(
    item: UserItem,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .combinedClickable(
                onClick = { /*do nothing*/ },
                onLongClick = onLongClick
            )
            .then(modifier)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = item.email,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Blue
        )

        Text(
            modifier = Modifier.align(Alignment.End),
            text = item.exists.toCreatedAgoText(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun PreviewUserItem() {
    UserItem(
        modifier = Modifier.fillMaxWidth(),
        item = UserItem(
            id = 1L,
            name = "Albus Percival Wulfric Brian Dumbledore",
            email = "albus.percival.wulfric.brian.dumbledore@protonmail.com",
            exists = 2.toDuration(DurationUnit.DAYS),
            createdAt = Long.MIN_VALUE
        ),
        onLongClick = {}
    )
}
