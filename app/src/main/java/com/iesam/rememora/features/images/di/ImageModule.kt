package com.iesam.rememora.features.images.di

import com.iesam.rememora.features.images.data.ImageDataRepository
import com.iesam.rememora.features.images.domain.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ImageModule {
    @Binds
    abstract fun bindImageDataRepository(imageDataRepository: ImageDataRepository): ImageRepository
}