package com.iesam.rememora.features.images.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(private val repository: ImageRepository) {

    suspend operator fun invoke(): Either<ErrorApp, List<Image>> {
        return repository.getImages()
    }
}