package com.iesam.rememora.features.music.di

import com.iesam.rememora.features.music.data.MusicDataRepository
import com.iesam.rememora.features.music.domain.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicModule {

}