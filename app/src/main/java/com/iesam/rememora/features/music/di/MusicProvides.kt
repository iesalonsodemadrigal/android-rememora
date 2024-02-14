package com.iesam.rememora.features.music.di

import com.iesam.rememora.BuildConfig
import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.music.data.MusicDataRepository
import com.iesam.rememora.features.music.data.local.MusicDao
import com.iesam.rememora.features.music.data.mock.MusicMockDataRepository
import com.iesam.rememora.features.music.domain.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class MusicProvides {
    @Provides
    fun provideMusicDao(appDatabase: AppDatabase): MusicDao = appDatabase.musicDao()

    @Provides
    fun provideMusicRepository(
        musicDataRepository: MusicDataRepository,
        mockDataRepository: MusicMockDataRepository
    ): MusicRepository {
        return if (BuildConfig.IS_LOCAL_ENV) {
            mockDataRepository
        } else {
            musicDataRepository
        }
    }
}