package com.iesam.rememora.features.video.di

import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.video.data.local.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class VideoProvides {
    @Provides
    fun provideVideoDao(appDatabase: AppDatabase): VideoDao = appDatabase.videoDao()
}