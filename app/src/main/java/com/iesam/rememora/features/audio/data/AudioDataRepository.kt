package com.iesam.rememora.features.audio.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.audio.data.local.AudioLocalDataSource
import com.iesam.rememora.features.audio.data.remote.AudioRemoteDataSource
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.AudiosRepository
import javax.inject.Inject

class AudioDataRepository @Inject constructor(
    private val remoteDataSource: AudioRemoteDataSource,
    private val localDataSource: AudioLocalDataSource
) : AudiosRepository {
    override suspend fun getAudios(uid: String): Either<ErrorApp, List<Audio>> {
        val localAudios = localDataSource.obtainAudios()
        return if (localAudios.isRight() && localAudios.get().isNotEmpty()) localAudios
        else {
            return remoteDataSource.getAudios(uid).map { audios ->
                localDataSource.deleteAllAudios()
                localDataSource.saveAudios(audios)
                audios
            }
        }
    }
}