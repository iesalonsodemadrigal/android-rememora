package com.iesam.rememora.features.music.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface MusicRepository {
    suspend fun obtainMusicList(): Either<ErrorApp, List<Music>>
}