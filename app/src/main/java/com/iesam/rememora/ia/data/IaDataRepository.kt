package com.iesam.rememora.ia.data

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.ia.domain.IaRepository
import javax.inject.Inject

class IaDataRepository @Inject constructor(private val gptDataSource: GptDataSource) :
    IaRepository {
    override suspend fun getIntention(prompt: String): Either<ErrorApp, String> {
        return gptDataSource.getIntention(prompt)
    }

}