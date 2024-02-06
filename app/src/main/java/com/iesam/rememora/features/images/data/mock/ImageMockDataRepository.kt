package com.iesam.rememora.features.images.data.mock

import android.content.Context
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.extensions.getFileFromAssets
import com.iesam.rememora.app.right
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageMockDataRepository @Inject constructor(@ApplicationContext val context: Context) :
    ImageRepository {

    override suspend fun getImages(): Either<ErrorApp, List<Image>> {
        //Photo 1
        val photo1 = Image(
            "Imagen 1",
            "1",
            context.getFileFromAssets("mock_photo_1.jpeg").absolutePath,
            "Imagen 1"
        )

        //Photo 2
        val photo2 = Image(
            "Imagen 2",
            "2",
            context.getFileFromAssets("mock_photo_2.jpeg").absolutePath,
            "Imagen 2"
        )

        return mutableListOf(photo1, photo2).right()
    }
}