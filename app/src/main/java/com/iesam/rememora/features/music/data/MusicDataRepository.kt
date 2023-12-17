package com.iesam.rememora.features.music.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.music.domain.Music
import com.iesam.rememora.features.music.domain.MusicRepostory

class MusicDataRepository(private val remoteDataSource: MusicRemoteDataSource) : MusicRepostory {
    override suspend fun obtainMusicList(): Either<ErrorApp, List<Music>> {
        return remoteDataSource.obtainMusicList()
    }
}