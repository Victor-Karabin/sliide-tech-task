package com.sliide.di.users

import com.sliide.data.users.UsersRepo
import com.sliide.data.users.UsersRepoImpl
import com.sliide.domain.common.ValidateEmailFormatCase
import com.sliide.domain.common.ValidateEmailFormatCaseImpl
import com.sliide.domain.users.ValidateUserInputCase
import com.sliide.domain.users.ValidateUserInputCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface UsersModule {

    @Binds
    fun bindUsersRepo(repo: UsersRepoImpl): UsersRepo

    @Binds
    fun bindValidateUserInputCase(case: ValidateUserInputCaseImpl): ValidateUserInputCase

    @Binds
    fun bindValidateEmailFormatCase(case: ValidateEmailFormatCaseImpl): ValidateEmailFormatCase
}
