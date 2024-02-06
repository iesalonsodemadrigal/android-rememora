package com.iesam.rememora.core.account.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.AppQualifier
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(@AppQualifier.Production private val repository: AccountRepository) {
    suspend operator fun invoke(): Either<ErrorApp, Boolean> {
        return repository.deleteAccount()
    }
}