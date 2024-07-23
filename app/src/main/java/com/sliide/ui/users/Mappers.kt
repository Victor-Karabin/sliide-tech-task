package com.sliide.ui.users

import com.sliide.boundary.users.User
import com.sliide.ui.users.models.UserItem

internal fun User.toItem(created: String = ""): UserItem {
    return UserItem(
        id = this.id,
        name = this.name,
        email = this.email,
        created = created
    )
}