package com.iesam.rememora.features.music.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class GetMusicListUseCase @Inject constructor(private val musicRepository: MusicRepostory) {
    suspend operator fun invoke(): Either<ErrorApp, List<Music>> {
        return musicRepository.obtainMusicList()
    }
}