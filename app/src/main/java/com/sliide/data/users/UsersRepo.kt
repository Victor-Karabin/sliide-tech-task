package com.sliide.data.users

import com.sliide.domain.users.models.User

interface UsersRepo {

    suspend fun lastUsers(): Result<List<User>>

    suspend fun users(): Result<List<User>>

    suspend fun createUser(user: User): Result<User>

    suspend fun deleteUser(id: Long): Result<Unit>
}
