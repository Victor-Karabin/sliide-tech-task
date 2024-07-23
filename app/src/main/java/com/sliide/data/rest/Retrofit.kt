package com.sliide.data.rest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

internal fun <T> Response<T>.toException(): Exception {
    return IllegalStateException(collectLogData())
}

private fun <T> Response<T>.collectLogData(): String {
    val raw = this.raw()

    return StringBuilder()
        .append("\nrequest: ")
        .append(raw.request.url)
        .append("\nresponse: ")
        .append(code())
        .append("\n")
        .append(errorBody()?.string() ?: body()?.toString())
        .toString()
}

internal suspend fun <T> wrapRequest(
    dispatcher: CoroutineDispatcher,
    request: suspend () -> Response<T>
): Result<T> {
    return withContext(dispatcher) {
        try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(response.toException())
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}

internal suspend fun <T> wrapRequestNullableBody(
    dispatcher: CoroutineDispatcher,
    request: suspend () -> Response<T>
): Result<T?> {
    return withContext(dispatcher) {
        try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful) {
                Result.success(body)
            } else {
                Result.failure(response.toException())
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}