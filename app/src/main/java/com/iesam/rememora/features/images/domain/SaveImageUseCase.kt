package com.iesam.rememora.features.images.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {

    suspend operator fun invoke(
        image: Image?,
        reaction: Int
    ): Either<ErrorApp, Boolean> {
        return if (image != null) {
            val input = Input(image.id, image.title, image.description, image.source, reaction)
            imageRepository.saveImage(input)
        } else {
            ErrorApp.UnknownError.left()
        }
    }

    data class Input(
        val id: String,
        val title: String,
        val description: String,
        val image: String,
        val reaction: Int
    )

}