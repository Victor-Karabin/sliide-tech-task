package com.sliide.data.users

import com.sliide.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

internal class UsersApiProviderImpl @Inject constructor(
    private val baseUrl: String,
    private val accessToken: String
) : UsersApiProvider {

    private val retrofit by lazy {
        val clientBuilder = OkHttpClient()
            .newBuilder()
            .addInterceptor(UsersAuthInterceptor(accessToken))

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            clientBuilder.addInterceptor(loggingInterceptor)
        }

        val client = clientBuilder.build()
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    private val usersApi by lazy {
        retrofit.create(UsersApi::class.java)
    }

    override fun provideUsersApi(): UsersApi = usersApi
}
