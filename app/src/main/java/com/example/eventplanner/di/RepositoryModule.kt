package com.example.eventplanner.di

import com.example.eventplanner.data.EventDao
import com.example.eventplanner.data.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideRepository(dao: EventDao): EventRepository = EventRepository(dao)
}