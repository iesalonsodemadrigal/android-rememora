package com.iesam.rememora.features.music.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.music.domain.Music
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MusicRemoteDataSource @Inject constructor() {
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
                music.source =
                    fireBaseStorage.getReferenceFromUrl(music.source!!).downloadUrl.await()
                        .toString()
                music.toModel()
            }.right()
        } catch (exception: Exception) {
            ErrorApp.NetworkError.left()
        }
    }
}