package com.sliide.data.users

import com.sliide.boundary.users.User
import com.sliide.boundary.users.UsersRepo
import com.sliide.data.rest.wrapRequest
import com.sliide.data.rest.wrapRequestNullableBody
import com.sliide.di.coroutines.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class UsersRepoImpl @Inject constructor(
    private val api: UsersApi,
    @IODispatcher
    private val io: CoroutineDispatcher
) : UsersRepo {

    override suspend fun users(): Result<List<User>> {
        return wrapRequest(io) { api.listUsers() }
            .map { users -> users.map { dto -> dto.toUser() } }
    }

    override suspend fun createUser(user: User): Result<User> {
        return wrapRequest(io) { api.create(user.toDto(DEF_GENDER, DEF_STATUS)) }
            .map { dto -> dto.toUser() }
    }

    override suspend fun deleteUser(id: Long): Result<Unit> {
        return wrapRequestNullableBody(io) { api.delete(id) }
            .map { /*do nothing*/ }
    }

    companion object {
        private const val DEF_GENDER = "male"
        private const val DEF_STATUS = "inactive"
    }
}