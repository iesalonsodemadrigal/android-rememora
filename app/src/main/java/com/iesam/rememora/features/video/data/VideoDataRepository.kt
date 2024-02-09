package com.iesam.rememora.features.video.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.video.data.local.VideoLocalDataSource
import com.iesam.rememora.features.video.data.remote.VideoRemoteDataSource
import com.iesam.rememora.features.video.domain.Video
import com.iesam.rememora.features.video.domain.VideoRepository
import javax.inject.Inject

class VideoDataRepository @Inject constructor(
    private val remoteDataSource: VideoRemoteDataSource,
    private val local: VideoLocalDataSource
) :
    VideoRepository {
    override suspend fun getVideos(): Either<ErrorApp, List<Video>> {
        val videosLocal = local.obtain()
        return if (videosLocal.isRight() && videosLocal.get().isNotEmpty()) {
            videosLocal
        } else {
            return remoteDataSource.getVideos().map {
                local.delete()
                local.save(it)
                it
            }
        }
    }

}