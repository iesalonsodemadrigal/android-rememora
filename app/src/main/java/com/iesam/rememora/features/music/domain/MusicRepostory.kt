package com.iesam.rememora.features.music.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface MusicRepostory {
    suspend fun obtainMusicList(uid: String): Either<ErrorApp, List<Music>>
}