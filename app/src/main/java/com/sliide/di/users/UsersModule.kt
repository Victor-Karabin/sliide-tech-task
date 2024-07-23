package com.sliide.di.users

import com.sliide.boundary.users.UsersRepo
import com.sliide.data.users.UsersRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface UsersModule {

    @Binds
    fun bindUsersRepo(repo: UsersRepoImpl): UsersRepo
}