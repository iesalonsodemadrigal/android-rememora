package com.iesam.rememora.features.video.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.core.account.domain.AccountRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Either<ErrorApp, List<Video>> {
        val user = accountRepository.getAccount()
        return user.map { userDb ->
            return if (userDb != null) {
                videoRepository.getVideos(userDb.id!!)
            } else {
                ErrorApp.SessionError.left()
            }
        }
    }
}