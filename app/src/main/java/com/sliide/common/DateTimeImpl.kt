package com.sliide.common

import javax.inject.Inject

class DateTimeImpl @Inject constructor() : DateTime {

    override fun currentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}