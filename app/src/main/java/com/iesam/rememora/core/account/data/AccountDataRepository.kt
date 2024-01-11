package com.iesam.rememora.core.account.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.account.domain.Account
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class AccountDataRepository @Inject constructor(private val firebase: AccountFirebaseRemoteDataSource) :
    AccountRepository {
    override suspend fun logout(): Either<ErrorApp, Boolean> {
        return firebase.logout()
    }

    override suspend fun deleteAccount(): Either<ErrorApp, Boolean> {
        return firebase.deleteAccount()
    }

    override suspend fun getAccount(): Either<ErrorApp, Account?> {
        return firebase.getAccount()
    }
}