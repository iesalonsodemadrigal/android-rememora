package com.iesam.rememora.core.account.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): Either<ErrorApp, Boolean> {
        return repository.deleteAccount()
    }
}