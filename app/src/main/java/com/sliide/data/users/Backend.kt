package com.sliide.data.users

internal class Backend {

    companion object {
        const val EMAIL = "email"
        const val NAME = "name"
        const val IS_INVALID = "is invalid"
        const val CAN_NOT_BE_BLANK = "can't be blank"
        const val ALREADY_TAKEN = "has already been taken"

        const val DEF_GENDER = "male"
        const val DEF_STATUS = "inactive"

        const val PAGES_HEADER = "X-Pagination-Pages"
        const val LIMIT_HEADER = "X-Pagination-Limit"
    }
}
