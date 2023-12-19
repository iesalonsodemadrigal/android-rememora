package com.iesam.rememora.features.home.domain

import com.google.firebase.auth.FirebaseUser
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class IsAccountUseCase @Inject constructor(private val repository: AccountRepository) {
    operator fun invoke(): Either<ErrorApp, FirebaseUser?> {
        return repository.isAccount()
    }
}