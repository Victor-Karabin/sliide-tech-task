package com.sliide.di.users

import com.sliide.BuildConfig
import com.sliide.data.users.UsersApi
import com.sliide.data.users.UsersApiProvider
import com.sliide.data.users.UsersApiProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UsersApiModule {

    @Provides
    @Singleton
    fun provideUsersApiProvider(): UsersApiProvider {
        return UsersApiProviderImpl(BuildConfig.GO_REST_BASE_URL, BuildConfig.GO_AUTH_TOKEN)
    }

    @Provides
    @Singleton
    fun provideUsersApi(apiProvider: UsersApiProvider): UsersApi {
        return apiProvider.provideUsersApi()
    }
}
