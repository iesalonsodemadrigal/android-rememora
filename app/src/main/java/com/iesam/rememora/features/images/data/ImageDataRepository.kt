package com.iesam.rememora.features.images.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.ImageRepository
import javax.inject.Inject

class ImageDataRepository @Inject constructor(private val imageRemoteDataSource: ImageRemoteDataSource) :
    ImageRepository {
    override suspend fun getImages(uid: String): Either<ErrorApp, List<Image>> {
        return imageRemoteDataSource.getImages(uid)
    }
}