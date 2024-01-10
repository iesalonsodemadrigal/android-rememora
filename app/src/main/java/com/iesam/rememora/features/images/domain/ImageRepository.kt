package com.iesam.rememora.features.images.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface ImageRepository {
    suspend fun getImages(uid: String): Either<ErrorApp, List<Image>>
}