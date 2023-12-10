package com.iesam.rememora.features.images.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.ImageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor() : ImageRepository {
    override suspend fun getImages(): Either<ErrorApp, List<Image>> {
        val dataBase: FirebaseDatabase = FirebaseDatabase.getInstance()

        return try {
            val dataSnapshot = dataBase
                .getReference("users/user_1/photos/album_1")
                .get()
                .await()
            val images = dataSnapshot.children.map {
                it.getValue(Image::class.java)!!
            }
            images.map { image ->
                if (!image.source.isNullOrEmpty() && image.source?.get(0) == 'g') {
                    convertUrlImage(image.source!!) { newUrl ->
                        image.source = newUrl
                    }
                }
            }
            images.right()
        } catch (ex: Exception) {
            ErrorApp.UnknownError.left()
        }
    }

    private fun convertUrlImage(url: String, callback: (String) -> Unit) {
        val storage = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        storage.downloadUrl.addOnSuccessListener { uri ->
            callback(uri.toString())
        }
    }
}