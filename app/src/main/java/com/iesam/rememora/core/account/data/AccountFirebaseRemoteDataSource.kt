package com.iesam.rememora.core.account.data

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.core.account.domain.Account
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountFirebaseRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val authUi: AuthUI
) {

    suspend fun logout(): Either<ErrorApp, Boolean> {
        return try {
            authUi.signOut(context).await()
            true.right()
        } catch (e: Error) {
            ErrorApp.ServerError.left()
        }
    }

    suspend fun deleteAccount(): Either<ErrorApp, Boolean> {
        return try {
            authUi.delete(context).await()
            true.right()
        } catch (e: Error) {
            ErrorApp.ServerError.left()
        }
    }

    fun getAccount(): Either<ErrorApp, Account?> {
        return try {
            val account = auth.currentUser?.toModel()
            account.right()
        } catch (e: Error) {
            ErrorApp.ServerError.left()
        }
    }
}