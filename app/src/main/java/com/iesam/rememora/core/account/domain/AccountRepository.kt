package com.iesam.rememora.core.account.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface AccountRepository {
    suspend fun signOut(): Either<ErrorApp, Boolean>
    suspend fun deleteAccount(): Either<ErrorApp, Boolean>
    suspend fun getAccount(): Either<ErrorApp, Account?>
}