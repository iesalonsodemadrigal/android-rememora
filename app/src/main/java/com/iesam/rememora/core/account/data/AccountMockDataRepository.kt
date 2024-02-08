package com.iesam.rememora.core.account.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.right
import com.iesam.rememora.core.account.domain.Account
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class AccountMockDataRepository @Inject constructor() :
    AccountRepository {

    override suspend fun signOut(): Either<ErrorApp, Boolean> {
        return true.right()
    }

    override suspend fun deleteAccount(): Either<ErrorApp, Boolean> {
        return true.right()
    }

    override suspend fun getAccount(): Either<ErrorApp, Account?> {
        return Account("1", "Usuario Mock", "siadenlab@iesalonsodemadrigal.es").right()
    }
}