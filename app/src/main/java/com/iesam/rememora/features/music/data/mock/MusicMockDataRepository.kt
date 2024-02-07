package com.iesam.rememora.features.music.data.mock

import android.content.Context
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.extensions.getFileFromAssets
import com.iesam.rememora.app.right
import com.iesam.rememora.features.music.domain.MusicRepository
import com.iesam.rememora.features.music.domain.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicMockDataRepository @Inject constructor(@ApplicationContext val context: Context) :
    MusicRepository {
    override suspend fun obtainMusicList(uid: String): Either<ErrorApp, List<Song>> {
        return mutableListOf(
            Song(
                "1",
                "Cancion 1",
                context.getFileFromAssets("music", "mock_song_1.mp3").absolutePath,
                "Cancion 1"
            ),
            Song(
                "2",
                "Cancion 2",
                context.getFileFromAssets("music", "mock_song_2.mp3").absolutePath,
                "Cancion 2"
            ),
            Song(
                "3",
                "Cancion 3",
                context.getFileFromAssets("music", "mock_song_3.mp3").absolutePath,
                "Cancion 3"
            ),
            Song(
                "4",
                "Cancion 4",
                context.getFileFromAssets("music", "mock_song_4.mp3").absolutePath,
                "Cancion 4"
            ),
            Song(
                "5",
                "Cancion 5",
                context.getFileFromAssets("music", "mock_song_5.mp3").absolutePath,
                "Cancion 5"
            )
        ).right()
    }
}