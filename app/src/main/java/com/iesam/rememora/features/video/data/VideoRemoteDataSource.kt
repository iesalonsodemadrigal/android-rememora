package com.iesam.rememora.features.video.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.video.domain.Video
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRemoteDataSource @Inject constructor(private val database: FirebaseDatabase,
        private val storage: FirebaseStorage) {
    suspend fun getVideos(): Either<ErrorApp, List<Video>> {
        return try {
            val snapshot = database
                .getReference("users/user_1/videos/videos_1")
                .get()
                .await()
            snapshot.children.map {
                it.getValue(VideoApiModel::class.java)!!
            }.map { video ->
                video.source =
                    storage.getReferenceFromUrl(video.source!!).downloadUrl.await().toString()
                video.toModel()
            }.right()
        } catch (exception: Exception) {
            return ErrorApp.DataError.left()
        }
    }

}