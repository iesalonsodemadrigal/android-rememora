package com.iesam.rememora.ia.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp

interface IaRepository {
    suspend fun getIntention(prompt: String): Either<ErrorApp, String>
}