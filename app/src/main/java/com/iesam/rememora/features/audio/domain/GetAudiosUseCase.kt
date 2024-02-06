package com.iesam.rememora.features.audio.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.AppQualifier
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class GetAudiosUseCase @Inject constructor(
    private val repository: AudiosRepository,
    @AppQualifier.Mock private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Either<ErrorApp, List<Audio>> {
        val user = accountRepository.getAccount()
        return user.map { userDb ->
            return if (userDb != null) {
                repository.getAudios(userDb.id!!)
            } else {
                ErrorApp.SessionError.left()
            }
        }
    }
}