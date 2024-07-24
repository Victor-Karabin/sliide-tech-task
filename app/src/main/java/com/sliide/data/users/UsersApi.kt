package com.sliide.data.users

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface UsersApi {

    @GET("/public/v2/users")
    suspend fun listUsers(): Response<List<UserDto>>

    @POST("/public/v2/users")
    suspend fun create(@Body user: UserDto): Response<UserDto>

    @DELETE("/public/v2/users/{userId}")
    suspend fun delete(@Path("userId") userId: Long): Response<Unit>
}
