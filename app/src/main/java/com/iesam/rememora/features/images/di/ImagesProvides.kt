package com.iesam.rememora.features.images.di

import com.iesam.rememora.BuildConfig
import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.images.data.ImageDataRepository
import com.iesam.rememora.features.images.data.local.ImageDao
import com.iesam.rememora.features.images.data.mock.ImageMockDataRepository
import com.iesam.rememora.features.images.domain.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ImagesProvides {

    @Provides
    fun provideImageDao(appDataBase: AppDatabase): ImageDao = appDataBase.imageDao()

    @Provides
    fun providePhotoRepository(
        dataRepository: ImageDataRepository,
        mockDataRepository: ImageMockDataRepository
    ): ImageRepository {
        return if (BuildConfig.IS_LOCAL_ENV) {
            mockDataRepository
        } else {
            dataRepository
        }
    }

}