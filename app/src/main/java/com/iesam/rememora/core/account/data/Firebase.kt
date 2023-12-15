package com.iesam.rememora.core.account.data

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.core.account.domain.AccountRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Firebase @Inject constructor(@ApplicationContext private val context: Context) :
    AccountRepository {
    override suspend fun logout(): Either<ErrorApp, Boolean> {
        return try {
            AuthUI.getInstance().signOut(context).await()
            true.right()
        } catch (e: Error) {
            ErrorApp.UnknownError.left()
        }
    }

    override suspend fun deleteAccount(): Either<ErrorApp, Boolean> {
        return try {
            AuthUI.getInstance().delete(context).await()
            true.right()
        } catch (e: Error) {
            ErrorApp.UnknownError.left()
        }
    }
}