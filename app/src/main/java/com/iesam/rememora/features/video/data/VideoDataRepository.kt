package com.iesam.rememora.features.video.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.video.domain.Video
import com.iesam.rememora.features.video.domain.VideoRepository

class VideoDataRepository(private val remoteDataSource: VideoRemoteDataSource) : VideoRepository {
    override suspend fun getVideos(): Either<ErrorApp, List<Video>> {
        return remoteDataSource.getVideos()
    }

}