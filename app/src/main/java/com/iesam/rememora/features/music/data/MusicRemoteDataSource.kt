package com.iesam.rememora.features.music.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.music.domain.Music
import kotlinx.coroutines.tasks.await
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MusicRemoteDataSource @Inject constructor(
    private val fireBaseDB: FirebaseDatabase,
    private val fireBaseStorage: FirebaseStorage
) {

    suspend fun obtainMusicList(uid: String): Either<ErrorApp, List<Music>> {
        return try {
            val dataSnapshot = fireBaseDB
                .getReference("users/${uid}/music/playlist1")
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