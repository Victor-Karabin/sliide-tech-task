package com.sliide.data.rest

internal class RestThrowable(val code: Int, val url: String, val body: String?) : Throwable()
