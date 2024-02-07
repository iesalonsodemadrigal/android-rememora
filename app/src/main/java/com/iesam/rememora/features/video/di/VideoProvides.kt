package com.iesam.rememora.features.video.di

import com.iesam.rememora.BuildConfig
import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.video.data.VideoDataRepository
import com.iesam.rememora.features.video.data.local.VideoDao
import com.iesam.rememora.features.video.data.mock.VideoMockDataRepository
import com.iesam.rememora.features.video.domain.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class VideoProvides {
    @Provides
    fun provideVideoDao(appDatabase: AppDatabase): VideoDao = appDatabase.videoDao()

    @Provides
    fun provideVideoRepository(
        dataRepository: VideoDataRepository,
        mockDataRepository: VideoMockDataRepository
    ): VideoRepository {
        return if (BuildConfig.IS_LOCAL_ENV) {
            mockDataRepository
        } else {
            dataRepository
        }
    }
}