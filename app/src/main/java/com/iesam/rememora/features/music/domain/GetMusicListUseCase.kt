package com.iesam.rememora.features.music.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

class GetMusicListUseCase(private val musicRepostory: MusicRepostory) {
    operator suspend fun invoke(): Either<ErrorApp, List<Music>> {
        return musicRepostory.obtainMusicList()
    }
}