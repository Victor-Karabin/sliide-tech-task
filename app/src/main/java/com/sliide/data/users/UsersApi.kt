package com.sliide.data.users

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface UsersApi {

    @GET("/users")
    suspend fun listUsers(): Response<List<UserDto>>

    @POST("/users")
    suspend fun create(user: UserDto): Response<UserDto>

    @DELETE("/users/{userId}")
    suspend fun delete(@Path("userId") userId: Long): Response<Unit>
}