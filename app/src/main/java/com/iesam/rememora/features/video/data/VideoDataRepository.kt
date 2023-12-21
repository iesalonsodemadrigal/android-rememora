package com.iesam.rememora.features.video.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.video.domain.Video
import com.iesam.rememora.features.video.domain.VideoRepository
import javax.inject.Inject

class VideoDataRepository @Inject constructor(private val remoteDataSource: VideoRemoteDataSource) :
    VideoRepository {
    override suspend fun getVideos(uid: String): Either<ErrorApp, List<Video>> {
        return remoteDataSource.getVideos(uid)
    }

}