package com.iesam.rememora.features.audio.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.AudioDbModel
import com.iesam.rememora.features.audio.domain.toModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AudioRemoteDataSource @Inject constructor () {

    private val dataBase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
     suspend fun getAudios(): Either<ErrorApp, List<Audio>> {
        return try {
            val dataSnapshot = dataBase
                .getReference("users/user_1/audio/playlist2")
                .get()
                .await()
            dataSnapshot.children.map {
                it.getValue(AudioDbModel::class.java)!!
            }.map { audio ->
                audio.source = storage.getReferenceFromUrl(audio.source!!).downloadUrl.await().toString()
                audio.toModel()
            }.right()
        } catch (exception: Exception) {
            ErrorApp.DataError.left()
        }
    }
}