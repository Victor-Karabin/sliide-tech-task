package com.sliide.data.users

import com.squareup.moshi.Moshi

internal interface UsersApiProvider {

    fun provideUsersApi(): UsersApi

    fun provideMoshi(): Moshi
}
