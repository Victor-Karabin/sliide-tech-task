package com.sliide.boundary.users

interface UsersRepo {

    suspend fun users(): Result<List<User>>

    suspend fun createUser(user: User): Result<User>

    suspend fun deleteUser(id: Long): Result<Unit>
}