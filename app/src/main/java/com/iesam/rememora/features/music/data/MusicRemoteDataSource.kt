package com.iesam.rememora.features.music.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.music.domain.Music
import kotlinx.coroutines.tasks.await

class MusicRemoteDataSource() {
    private val fireBaseDB = FirebaseDatabase.getInstance()
    private val fireBaseStorage = FirebaseStorage.getInstance()
    suspend fun obtainMusicList(): Either<ErrorApp, List<Music>> {
        return try {
            val dataSnapshot = fireBaseDB
                .getReference("users/user_1/music/playlist2")
                .get()
                .await()
            dataSnapshot.children.map {
                it.getValue(MusicDBModel::class.java)!!
            }.map { music ->
                //Cambiar sintaxis para no cambiar a var la variable source de MusicDB
                music.source =
                    fireBaseStorage.getReferenceFromUrl(music.source!!).downloadUrl.await()
                        .toString()
                music.toModel()
            }.right()
        } catch (exception: Exception) {
            Log.d("@DEV", exception.message!!)
            //FirebaseCrashlytics.getInstance().recordException(exception)
            ErrorApp.UnknownError.left()
        }
    }
}