package com.iesam.rememora.features.music.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.music.data.local.MusicLocalDataSource
import com.iesam.rememora.features.music.data.remote.MusicRemoteDataSource
import com.iesam.rememora.features.music.domain.Music
import com.iesam.rememora.features.music.domain.MusicRepository
import javax.inject.Inject

class MusicDataRepository @Inject constructor(
    private val remoteDataSource: MusicRemoteDataSource,
    private val localDataSource: MusicLocalDataSource
) :
    MusicRepository {
    override suspend fun obtainMusicList(uid: String): Either<ErrorApp, List<Music>> {
        val localResult = localDataSource.getAllMusic()
        return if (localResult.isRight() && localResult.get().isNotEmpty()) {
            localResult
        } else {
            val remoteResult = remoteDataSource.obtainMusicList(uid)
            localDataSource.saveMusic(remoteResult.get())
            remoteResult
        }
    }

}