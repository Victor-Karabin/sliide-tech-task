package com.sliide.data.users

import com.sliide.common.flatMap
import com.sliide.common.mapFailure
import com.sliide.data.rest.RestThrowable
import com.sliide.data.rest.toThrowable
import com.sliide.data.rest.wrapRequest
import com.sliide.data.rest.wrapRequestNullableBody
import com.sliide.di.coroutines.IODispatcher
import com.sliide.domain.users.models.CreateUserThrowable
import com.sliide.domain.users.models.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

internal class UsersRepoImpl @Inject constructor(
    private val api: UsersApi,
    @IODispatcher
    private val io: CoroutineDispatcher
) : UsersRepo {

    private val moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    override suspend fun lastUsers(): Result<List<User>> {
        return fetchPageParams()
            .flatMap { params ->
                val (total, limit) = params
                wrapRequest(io) { api.listUsers(total, limit) }
            }
            .map { users -> users.map { dto -> dto.toUser() } }
    }

    private suspend fun fetchPageParams(): Result<Pair<Int, Int>> {
        return withContext(io) {
            try {
                val response = api.listUsers()
                if (response.isSuccessful) {
                    val headers = response.headers()
                    val totalPages = headers[Backend.PAGES_HEADER]?.toInt() ?: 1
                    val paginationLimit = headers[Backend.LIMIT_HEADER]?.toInt() ?: 10
                    Result.success(totalPages to paginationLimit)
                } else {
                    Result.failure(response.toThrowable())
                }
            } catch (ex: HttpException) {
                Result.failure(ex)
            } catch (ex: UnknownHostException) {
                Result.failure(ex)
            }
        }
    }

    override suspend fun users(): Result<List<User>> {
        return wrapRequest(io) { api.listUsers() }
            .map { users -> users.map { dto -> dto.toUser() } }
    }

    override suspend fun createUser(user: User): Result<User> {
        val create = user.toDto(Backend.DEF_GENDER, Backend.DEF_STATUS)

        return wrapRequest(io) { api.create(create) }
            .map { dto -> dto.toUser() }
            .mapFailure { ex ->
                val throwable = if (ex is RestThrowable) {
                    val setClass = Set::class.java
                    val errorDtoClass = FieldErrorDto::class.java
                    val type = Types.newParameterizedType(setClass, errorDtoClass)
                    val adapter = moshi.adapter<Set<FieldErrorDto>>(type)

                    ex.body
                        ?.let { body -> adapter.fromJson(body) }
                        ?.map { error -> error.toCreateUserError() }
                        ?.toSet()
                        ?.let { errors -> CreateUserThrowable(errors) } ?: ex
                } else ex

                Result.failure(throwable)
            }
    }

    override suspend fun deleteUser(id: Long): Result<Unit> {
        return wrapRequestNullableBody(io) { api.delete(id) }
            .map { /*do nothing*/ }
    }
}
