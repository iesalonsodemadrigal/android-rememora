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

class MusicRemoteDataSource @Inject constructor(
    private val fireBaseDB: FirebaseDatabase,
    private val fireBaseStorage: FirebaseStorage
) {

    suspend fun obtainMusicList(): Either<ErrorApp, List<Music>> {
        return try {
            val dataSnapshot = fireBaseDB
                .getReference("users/user_1/music/playlist3")
                .get()

            if (dataSnapshot.isSuccessful) {
                val musicList = dataSnapshot.result?.children?.map {
                    it.getValue(MusicDBModel::class.java)!!
                }?.map { music ->

                    music.source =
                        fireBaseStorage.getReferenceFromUrl(music.source!!)
                            .downloadUrl.await().toString()
                    music.toModel()

                }

                musicList?.right() ?: ErrorApp.ServerError.left()
            } else {
                // Handle the case when the dataSnapshot retrieval is not successful
                ErrorApp.ServerError.left()
            }
        } catch (exception: Exception) {
            // Handle other exceptions, e.g., Firebase database operation exception
            ErrorApp.ServerError.left()
        }
    }

}