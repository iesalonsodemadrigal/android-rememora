package com.iesam.rememora.features.audio.data

import com.google.firebase.database.FirebaseDatabase
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.AudiosRepository
import kotlinx.coroutines.tasks.await

class RemoteDataSource () : AudiosRepository{
    override suspend fun getAudios(): Either<ErrorApp, List<Audio>> {
        val dataBase : FirebaseDatabase = FirebaseDatabase.getInstance();
        return try {
            val dataSnapshot = dataBase
                .getReference("users/user_1/audio/playlist1")
                .get()
                .await()
            dataSnapshot.children.map { itemDataSnapshot ->
                itemDataSnapshot.getValue(Audio::class.java)!!
            }.right()
        } catch (exception: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(exception)
            ErrorApp.DataError.left()
        }
    }
}