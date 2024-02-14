package com.iesam.rememora.features.images.data.local

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.LocalModule.TIME_CACHE
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.images.domain.Image
import javax.inject.Inject

class ImageLocalDataSource @Inject constructor(private val imageDao: ImageDao) {

    suspend fun saveImage(images: List<Image>): Either<ErrorApp, Boolean> {
        val ms = System.currentTimeMillis()
        return try {
            val entityImage = images.map { image ->
                image.toEntity(ms)
            }
            imageDao.insertAll(*entityImage.toTypedArray())
            true.right()
        } catch (ex: Exception) {
            return ErrorApp.DataError.left()
        }
    }

    suspend fun getImages(): Either<ErrorApp, List<Image>> {
        return try {
            val images = imageDao.getAll()
            if (images.isEmpty() || images.first().createdAt.plus(TIME_CACHE) < System.currentTimeMillis()) {
                listOf<Image>().right()
            } else {
                images.map { image ->
                    image.toDomain()
                }.right()
            }
        } catch (ex: Exception) {
            ErrorApp.DataError.left()
        }
    }

    suspend fun deleteAllImages(): Either<ErrorApp, Boolean> {
        return try {
            imageDao.deleteAll()
            true.right()
        } catch (ex: Exception) {
            return ErrorApp.DataError.left()
        }
    }
}