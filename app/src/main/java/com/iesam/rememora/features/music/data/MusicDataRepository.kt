package com.iesam.rememora.features.music.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.music.data.remote.MusicRemoteDataSource
import com.iesam.rememora.features.music.domain.Music
import com.iesam.rememora.features.music.domain.MusicRepostory
import javax.inject.Inject

class MusicDataRepository @Inject constructor(private val remoteDataSource: MusicRemoteDataSource) :
    MusicRepostory {
    override suspend fun obtainMusicList(uid: String): Either<ErrorApp, List<Music>> {
        return remoteDataSource.obtainMusicList(uid)
    }
}