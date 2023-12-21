package com.iesam.rememora.features.video.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    suspend operator fun invoke(): Either<ErrorApp, List<Video>> {
        return videoRepository.getVideos()
    }
}