package com.iesam.rememora.features.images.di

import com.iesam.rememora.app.data.local.AppDatabase
import com.iesam.rememora.features.images.data.local.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ImagesProvides {

    @Provides
    fun provideImageDao(appDatBase: AppDatabase): ImageDao = appDatBase.imageDao()

}