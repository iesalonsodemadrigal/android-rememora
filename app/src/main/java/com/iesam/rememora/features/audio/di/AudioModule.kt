package com.iesam.rememora.features.audio.di

import com.iesam.rememora.features.audio.data.AudioDataRepository
import com.iesam.rememora.features.audio.domain.AudiosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AudioModule {
    @Binds
    abstract fun bindFirebase(dataRepository: AudioDataRepository): AudiosRepository
}