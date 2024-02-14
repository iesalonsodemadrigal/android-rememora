package com.iesam.rememora.features.audio.di

import com.iesam.rememora.BuildConfig
import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.audio.data.AudioDataRepository
import com.iesam.rememora.features.audio.data.local.AudioDao
import com.iesam.rememora.features.audio.data.mock.AudiosMockDataRepository
import com.iesam.rememora.features.audio.domain.AudiosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AudioProvides {
    @Provides
    fun provideAudioDao(appDatabase: AppDatabase): AudioDao = appDatabase.audioDao()

    @Provides
    fun provideAudioRepository(
        dataRepository:AudioDataRepository,
        mockDataRepository: AudiosMockDataRepository
    ):AudiosRepository{
        return if (BuildConfig.IS_LOCAL_ENV){
            mockDataRepository
        }else{
            dataRepository
        }
    }
}