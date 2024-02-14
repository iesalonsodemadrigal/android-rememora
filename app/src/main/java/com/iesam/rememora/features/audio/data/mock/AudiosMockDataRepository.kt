package com.iesam.rememora.features.audio.data.mock

import android.content.Context
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.extensions.getFileFromAssets
import com.iesam.rememora.app.right
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.AudiosRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AudiosMockDataRepository @Inject constructor(@ApplicationContext val context: Context) :
    AudiosRepository {
    override suspend fun getAudios(): Either<ErrorApp, List<Audio>> {
        val audio1 = Audio(
            "1",
            "Audio 1",
            context.getFileFromAssets("audios", "mock_audio_1.mp3").absolutePath,
            "Audio 1"
        )

        val audio2 = Audio(
            "2",
            "Audio 2",
            context.getFileFromAssets("audios", "mock_audio_2.mp3").absolutePath,
            "Audio 2"
        )

        val audio3 = Audio(
            "3",
            "Audio 3",
            context.getFileFromAssets("audios", "mock_audio_3.mp3").absolutePath,
            "Audio 3"
        )

        val audio4 = Audio(
            "4",
            "Audio 4",
            context.getFileFromAssets("audios", "mock_audio_4.mp3").absolutePath,
            "Audio 4"
        )

        val audio5 = Audio(
            "5",
            "Audio 5",
            context.getFileFromAssets("audios", "mock_audio_5.mp3").absolutePath,
            "Audio 5"
        )

        return mutableListOf(audio1, audio2, audio3, audio4, audio5).right()

    }

}