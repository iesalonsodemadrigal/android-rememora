package com.iesam.rememora.features.video.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface VideoRepository {
    suspend fun getVideos(uid: String): Either<ErrorApp, List<Video>>
}