package com.iesam.rememora.features.audio.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class GetAudiosUseCase @Inject constructor(
    private val repository: AudiosRepository
) {
    suspend operator fun invoke(): Either<ErrorApp, List<Audio>> {
        return repository.getAudios()
    }
}