package com.iesam.rememora.features.video.di

import com.iesam.rememora.features.video.data.VideoDataRepository
import com.iesam.rememora.features.video.domain.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class VideoModule {
    @Binds
    abstract fun bindVideoDataRepository(videoDataRepository: VideoDataRepository): VideoRepository
}