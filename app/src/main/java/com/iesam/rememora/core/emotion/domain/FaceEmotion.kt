package com.iesam.rememora.core.emotion.domain

data class FaceEmotion(
    val smilingProbability: Float = 0.0f,
    val leftEyeOpenProbability: Float = 0.0f,
    val rightEyeOpenProbability: Float = 0.0f
) {

    fun analize(): Int {
        if (isHappy()) {
            return 0
        } else if (isSad()) {
            return 1
        } else {
            return 2
        }
    }

    fun isHappy() = smilingProbability >= 0.60.toFloat()

    fun isSad() = smilingProbability < 0.20.toFloat()
            && (leftEyeOpenProbability <= 0.50.toFloat())
            && (rightEyeOpenProbability <= 0.50.toFloat())

    fun isNeutral() = !isHappy() && !isSad()
}