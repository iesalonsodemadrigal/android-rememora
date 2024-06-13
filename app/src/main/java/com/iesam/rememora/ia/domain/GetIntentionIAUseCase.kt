package com.iesam.rememora.ia.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class GetIntentionIAUseCase @Inject constructor(private val repository: IaRepository) {
    suspend operator fun invoke(prompt: String): Either<ErrorApp, String> {
        return repository.getIntention(prompt)
    }
}