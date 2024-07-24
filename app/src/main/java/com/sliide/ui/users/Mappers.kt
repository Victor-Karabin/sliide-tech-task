package com.sliide.ui.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.sliide.R
import com.sliide.domain.users.models.User
import com.sliide.ui.users.models.EmailError
import com.sliide.ui.users.models.NameError
import com.sliide.ui.users.models.UserItem
import kotlin.time.Duration

internal fun User.toItem(exists: Duration, createdAt: Long): UserItem {
    return UserItem(
        id = this.id,
        name = this.name,
        email = this.email,
        exists = exists,
        createdAt = createdAt
    )
}

@Composable
@ReadOnlyComposable
internal fun NameError.toText(): String {
    return when (this) {
        NameError.NameRequired -> stringResource(id = R.string.field_required)
        NameError.None -> ""
    }
}

@Composable
@ReadOnlyComposable
internal fun EmailError.toText(): String {
    return when (this) {
        EmailError.EmailRequired -> stringResource(id = R.string.field_required)
        EmailError.EmailFormat -> stringResource(id = R.string.email_invalid_format)
        EmailError.EmailExists -> stringResource(id = R.string.email_has_been_taken)
        EmailError.None -> ""
    }
}

@Composable
@ReadOnlyComposable
internal fun Duration.toCreatedAgoText(): String {
    return if (this.inWholeMinutes <= 0) {
        stringResource(id = R.string.now)
    } else {
        val pair = when {
            this.inWholeHours <= 0 -> R.plurals.minutes_short to this.inWholeMinutes.toInt()
            this.inWholeDays <= 0 -> R.plurals.hours to this.inWholeHours.toInt()
            else -> R.plurals.days to this.inWholeDays.toInt()
        }

        val ago = stringResource(id = R.string.ago)
        val unit = pluralStringResource(id = pair.first, count = pair.second)
        "${pair.second} $unit $ago"
    }
}