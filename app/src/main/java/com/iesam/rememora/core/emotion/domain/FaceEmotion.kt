package com.iesam.rememora.core.emotion.domain

data class FaceEmotion(
    val smilingProbability: Float = 0.0f,
    val leftEyeOpenProbability: Float = 0.0f,
    val rightEyeOpenProbability: Float = 0.0f
) {

    fun analize(): String {
        if (isHappy()) {
            return "Happy"
        } else if (isSad()) {
            return "Sad"
        } else {
            return "Neutral"
        }
    }

    fun isHappy() = smilingProbability >= 0.75.toFloat()

    fun isSad() = smilingProbability < 0.20.toFloat()
            && (leftEyeOpenProbability <= 0.50.toFloat())
            && (rightEyeOpenProbability <= 0.50.toFloat())

    fun isNeutral() = !isHappy() && !isSad()
}