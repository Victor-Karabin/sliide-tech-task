package com.sliide.di.utils

import com.sliide.common.DateTime
import com.sliide.common.DateTimeImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DateTimeModule {

    @Binds
    fun bindDateTime(dateTime: DateTimeImpl): DateTime
}