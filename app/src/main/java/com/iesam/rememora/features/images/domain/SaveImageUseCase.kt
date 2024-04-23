package com.iesam.rememora.features.images.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {

    suspend operator fun invoke(
        id: String,
        reaction: Int?
    ): Either<ErrorApp, Boolean> {
        return if (reaction != null) {
            val input = Input(id, reaction)
            imageRepository.saveImage(input)
        } else {
            ErrorApp.UnknownError.left()
        }
    }

    data class Input(
        val id: String,
        val reaction: Int
    )

}