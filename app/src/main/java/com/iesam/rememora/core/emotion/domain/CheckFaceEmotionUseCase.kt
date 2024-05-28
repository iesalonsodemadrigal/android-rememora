package com.iesam.rememora.core.emotion.domain

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.right
import javax.inject.Inject

class CheckFaceEmotionUseCase @Inject  constructor(){


    suspend operator fun invoke(input: Input): Either<ErrorApp, Int> {
        val faceEmotion = FaceEmotion(
            input.smilingProbability,
            input.leftEyeOpenProbability,
            input.rightEyeOpenProbability
        )
        return faceEmotion.analize().right()
    }

    data class Input(
        val smilingProbability: Float = 0.0f,
        val leftEyeOpenProbability: Float = 0.0f,
        val rightEyeOpenProbability: Float = 0.0f
    )
}