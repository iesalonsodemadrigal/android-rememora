package com.iesam.rememora.features.music.di

import com.iesam.rememora.app.data.AppDatabase
import com.iesam.rememora.features.music.data.local.MusicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class MusicProvides {
    @Provides
    fun provideMusicDao(appDatabase: AppDatabase): MusicDao = appDatabase.musicDao()
}