package com.iesam.rememora.features.images.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.images.data.local.ImageLocalDataSource
import com.iesam.rememora.features.images.data.remote.ImageRemoteDataSource
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.ImageRepository
import com.iesam.rememora.features.images.domain.SaveImageUseCase
import javax.inject.Inject

class ImageDataRepository @Inject constructor(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageRemoteDataSource: ImageRemoteDataSource
) :
    ImageRepository {
    override suspend fun getImages(): Either<ErrorApp, List<Image>> {
        val local = imageLocalDataSource.getImages()
        return if (local.isRight() && local.get().isNotEmpty()) {
            local
        } else {
            val remote = imageRemoteDataSource.getImages()
            remote.map { images ->
                imageLocalDataSource.deleteAllImages()
                imageLocalDataSource.saveImage(images)
            }
            remote
        }
    }

    override suspend fun saveImage(
        input: SaveImageUseCase.Input
    ): Either<ErrorApp, Boolean> {
        return imageRemoteDataSource.saveImage(input)
    }
}