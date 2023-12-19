package com.iesam.rememora.core.account.domain

import com.google.firebase.auth.FirebaseUser
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface AccountRepository {
    suspend fun logout(): Either<ErrorApp, Boolean>
    suspend fun deleteAccount(): Either<ErrorApp, Boolean>
    fun isAccount(): Either<ErrorApp, FirebaseUser?>
}