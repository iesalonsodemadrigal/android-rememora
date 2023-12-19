package com.iesam.rememora.core.account.data

import com.google.firebase.auth.FirebaseUser
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class FirebaseDataRepository @Inject constructor(private val firebase: FirebaseRemoteDataSource) :
    AccountRepository {
    override suspend fun logout(): Either<ErrorApp, Boolean> {
        return firebase.logout()
    }

    override suspend fun deleteAccount(): Either<ErrorApp, Boolean> {
        return firebase.deleteAccount()
    }

    override fun isAccount(): Either<ErrorApp, FirebaseUser?> {
        return firebase.isAccount()
    }
}