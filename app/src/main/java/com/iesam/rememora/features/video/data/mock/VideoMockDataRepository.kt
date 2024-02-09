package com.iesam.rememora.features.video.data.mock

import android.content.Context
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.extensions.getFileFromAssets
import com.iesam.rememora.app.right
import com.iesam.rememora.features.video.domain.Video
import com.iesam.rememora.features.video.domain.VideoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VideoMockDataRepository @Inject constructor(@ApplicationContext val context: Context) :
    VideoRepository {
    override suspend fun getVideos(): Either<ErrorApp, List<Video>> {
        val video1 = Video(
            "Vídeo 1",
            "1",
            context.getFileFromAssets("videos", "mock_video_1.mp4").absolutePath,
            "Vídeo 1"
        )
        val video2 = Video(
            "Vídeo 2",
            "2",
            context.getFileFromAssets("videos", "mock_video_2.mp4").absolutePath,
            "Vídeo 2"
        )
        val video3 = Video(
            "Vídeo 3",
            "3",
            context.getFileFromAssets("videos", "mock_video_3.mp4").absolutePath,
            "Vídeo 3"
        )
        val video4 = Video(
            "Vídeo 4",
            "4",
            context.getFileFromAssets("videos", "mock_video_4.mp4").absolutePath,
            "Vídeo 4"
        )
        val video5 = Video(
            "Vídeo 5",
            "5",
            context.getFileFromAssets("videos", "mock_video_5.mp4").absolutePath,
            "Vídeo 5"
        )

        return mutableListOf(video1, video2, video3, video4, video4).right()
    }

}