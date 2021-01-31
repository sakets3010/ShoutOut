package com.example.shoutout.di

import com.example.shoutout.ShoutRepository
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@dagger.Module
object DBModule {
    @dagger.Provides
    fun provideNightRepository() = ShoutRepository()
}