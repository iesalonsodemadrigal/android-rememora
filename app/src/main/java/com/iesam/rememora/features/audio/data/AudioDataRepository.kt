package com.iesam.rememora.features.audio.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.audio.data.remote.AudioRemoteDataSource
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.AudiosRepository
import javax.inject.Inject

class AudioDataRepository @Inject constructor (private val remoteDataSource: AudioRemoteDataSource) : AudiosRepository {
    override suspend fun getAudios(): Either<ErrorApp, List<Audio>> {
        return remoteDataSource.getAudios()
    }
}