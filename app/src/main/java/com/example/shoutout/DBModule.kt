package com.example.shoutout

import android.content.Context
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ApplicationComponent::class)
@dagger.Module
object DBModule {
    @dagger.Provides
    fun provideNightRepository() = ShoutRepository()
}