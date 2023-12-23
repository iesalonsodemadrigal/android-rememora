package com.iesam.rememora.core.account.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface AccountRepository {
    suspend fun logout(): Either<ErrorApp, Boolean>
    suspend fun deleteAccount(): Either<ErrorApp, Boolean>
    fun getAccount(): Either<ErrorApp, Account?>
}