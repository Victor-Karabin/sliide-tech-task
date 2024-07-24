package com.sliide.common

inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return this.mapCatching { payload: T ->
        transform(payload).getOrThrow()
    }
}

internal fun <T> Result<T>.mapFailure(transform: (Throwable) -> Result<T>): Result<T> {
    return if (this.isFailure) {
        try {
            transform(this.exceptionOrNull() ?: IllegalStateException("$this"))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    } else this
}
