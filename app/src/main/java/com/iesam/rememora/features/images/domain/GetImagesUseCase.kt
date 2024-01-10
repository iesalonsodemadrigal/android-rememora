package com.iesam.rememora.features.images.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val repository: ImageRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Either<ErrorApp, List<Image>> {
        val user = accountRepository.getAccount()
        return user.map { userDb ->
            return if (userDb != null) {
                repository.getImages(userDb.id!!)
            } else {
                ErrorApp.UnknownError.left()
            }
        }
    }
}