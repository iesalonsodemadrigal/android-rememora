package com.iesam.rememora.app.di

import android.content.Context
import androidx.room.Room
import com.iesam.rememora.app.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    const val TIME_CACHE = 5 * 60000
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "rememora"
    )
        .fallbackToDestructiveMigration()
        .build()
}