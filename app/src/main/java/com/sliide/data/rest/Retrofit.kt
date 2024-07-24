package com.sliide.data.rest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

internal fun <T> Response<T>.toThrowable(): Throwable {
    return RestThrowable(
        code = this.code(),
        url = this.raw().request.url.toString(),
        body = errorBody()?.string() ?: body()?.toString()
    )
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
                Result.failure(response.toThrowable())
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
                Result.failure(response.toThrowable())
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
