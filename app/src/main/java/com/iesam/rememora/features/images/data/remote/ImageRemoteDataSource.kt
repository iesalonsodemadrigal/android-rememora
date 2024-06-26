package com.iesam.rememora.features.images.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.SaveImageUseCase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(
    private val dataBase: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    suspend fun saveImage(input: SaveImageUseCase.Input): Either<ErrorApp, Boolean> {
        try {
            auth.uid?.let { uid ->
                var id = input.id.toLong()
                dataBase
                    .getReference("users")
                    .child("${uid}/photos/album_1/photo_$id")
                    .setValue(
                        input.toRemote()
                    ).await()
                return true.right()
            }
            return ErrorApp.ServerError.left()
        } catch (ex: Exception) {
            ex.message?.let {
                //FirebaseCrashlytics.getInstance().log(it)
            }
            return ErrorApp.ServerError.left()
        }
    }

    suspend fun getImages(): Either<ErrorApp, List<Image>> {
        return try {
            auth.uid?.let { uid ->
                val dataSnapshot = dataBase
                    .getReference("users/${uid}/photos/album_1")
                    .get()
                    .await()
                val imagesDb = dataSnapshot.children.map {
                    it.getValue(DownloadImageDbModel::class.java)!!
                }
                imagesDb.map { image ->
                    if (!image.source.isNullOrEmpty() && image.source?.get(0) == 'g') {
                        image.source = convertUrlImage(image.source!!)
                    }
                    image.toModel()
                }.right()
            } ?: run {
                ErrorApp.SessionError.left()
            }
        } catch (exception: Exception) {
            ErrorApp.ServerError.left()
        }
    }

    private suspend fun convertUrlImage(url: String): String {
        val storage = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        val uri = storage.downloadUrl.await()
        return uri.toString()
    }
}