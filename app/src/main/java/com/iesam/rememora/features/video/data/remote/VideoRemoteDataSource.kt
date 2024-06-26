package com.iesam.rememora.features.video.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.video.domain.Video
import kotlinx.coroutines.tasks.await
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class VideoRemoteDataSource @Inject constructor(
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    suspend fun getVideos(): Either<ErrorApp, List<Video>> {
        return try {
            auth.uid?.let { uid ->
                val snapshot = database
                    .getReference("users/${uid}/videos/videos_1")
                    .get()
                    .await()
                snapshot.children.map {
                    it.getValue(VideoApiModel::class.java)!!
                }.map { video ->
                    video.source =
                        storage.getReferenceFromUrl(video.source!!).downloadUrl.await().toString()
                    video.toModel()
                }.right()
            } ?: run {
                ErrorApp.SessionError.left()
            }
        } catch (ex: ConnectException) {
            ErrorApp.InternetError.left()
        } catch (ex: UnknownHostException) {
            ErrorApp.InternetError.left()
        } catch (ex: SocketTimeoutException) {
            ErrorApp.InternetError.left()
        } catch (exception: Exception) {
            ErrorApp.ServerError.left()
        }
    }

}