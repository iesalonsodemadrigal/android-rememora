package com.iesam.rememora.features.music.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class GetMusicListUseCase @Inject constructor(
    private val musicRepository: MusicRepostory,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Either<ErrorApp, List<Music>> {
        val user = accountRepository.getAccount()
        return user.map { userDb ->
            return if (userDb != null) {
                musicRepository.obtainMusicList(userDb.id!!)
            } else {
                ErrorApp.UnknownError.left()
            }
        }
    }
}