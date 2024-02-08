package com.iesam.rememora.features.audio.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface AudiosRepository {
    suspend fun getAudios(): Either<ErrorApp, List<Audio>>
}