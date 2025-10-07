package com.example.eventplanner.di

import android.content.Context
import com.example.eventplanner.data.EventDao
import com.example.eventplanner.data.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase {
        return EventDatabase.getInstance(context)
    }


    @Provides
    fun provideEventDao(db: EventDatabase): EventDao = db.eventDao()
}