package com.iesam.rememora.features.video.data.local

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.LocalModule.TIME_CACHE
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.video.domain.Video
import javax.inject.Inject

class VideoLocalDataSource @Inject constructor(private val videoDao: VideoDao) {
    suspend fun save(videos: List<Video>): Either<ErrorApp, Boolean> {
        return try {
            val time = System.currentTimeMillis()
            val videoEntity = videos.map {
                it.toEntity(time)
            }
            videoDao.insertAll(*videoEntity.toTypedArray())
            true.right()
        } catch (ex: Exception) {
            return ErrorApp.DataError.left()
        }
    }

    suspend fun obtain(): Either<ErrorApp, List<Video>> {
        return try {
            val videos = videoDao.getAll()
            if (videos.isEmpty() || videos[0].createdAt.plus(TIME_CACHE) < System.currentTimeMillis()) {
                listOf<Video>().right()
            } else {
                videos.map {
                    it.toModel()
                }.right()
            }
        } catch (ex: Exception) {
            return ErrorApp.DataError.left()
        }
    }

    suspend fun delete(): Either<ErrorApp, Boolean> {
        return try {
            videoDao.deleteAll()
            true.right()
        } catch (ex: Exception) {
            return ErrorApp.DataError.left()
        }
    }
}