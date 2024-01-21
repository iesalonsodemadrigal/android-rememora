package com.iesam.rememora.features.images.data.remote

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.images.domain.Image
import kotlinx.coroutines.tasks.await
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(private val dataBase: FirebaseDatabase) {
    suspend fun getImages(uid: String): Either<ErrorApp, List<Image>> {

        return try {
            val dataSnapshot = dataBase
                .getReference("users/${uid}/photos/album_1")
                .get()
                .await()
            val imagesDb = dataSnapshot.children.map {
                it.getValue(ImageDbModel::class.java)!!
            }
            imagesDb.map { image ->
                if (!image.source.isNullOrEmpty() && image.source?.get(0) == 'g') {
                    image.source = convertUrlImage(image.source!!)
                }
                image.toModel()
            }.right()
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

    private suspend fun convertUrlImage(url: String):String {
        val storage = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        val uri = storage.downloadUrl.await()
        return uri.toString()
    }
}