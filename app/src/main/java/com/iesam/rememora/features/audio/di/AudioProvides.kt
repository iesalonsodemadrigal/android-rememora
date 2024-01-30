package com.iesam.rememora.features.audio.di

import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.audio.data.local.AudioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AudioProvides {
    @Provides
    fun provideAudioDao(appDatabase: AppDatabase): AudioDao = appDatabase.audioDao()
}