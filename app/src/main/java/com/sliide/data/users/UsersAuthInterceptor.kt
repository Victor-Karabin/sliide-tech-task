package com.sliide.data.users

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UsersAuthInterceptor @Inject constructor(
    private val authToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val urlBuilder = original.url.newBuilder()
        val requestBuilder = original.newBuilder()
            .url(urlBuilder.build())
            .addHeader(AUTHORIZATION, "$BEARER $authToken")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }


    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }
}
