package com.iesam.rememora.features.audio.data.remote

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.audio.domain.Audio
import kotlinx.coroutines.tasks.await
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AudioRemoteDataSource @Inject constructor(
    private val firebaseDataBase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) {
    suspend fun getAudios(uid: String): Either<ErrorApp, List<Audio>> {
        return try {
            val dataSnapshot = firebaseDataBase
                .getReference("users/${uid}/audio/playlist1")
                .get()
                .await()
            dataSnapshot.children.map {
                it.getValue(AudioDbModel::class.java)!!
            }.map { audio ->
                audio.source =
                    firebaseStorage.getReferenceFromUrl(audio.source!!).downloadUrl.await()
                        .toString()
                audio.toModel()
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