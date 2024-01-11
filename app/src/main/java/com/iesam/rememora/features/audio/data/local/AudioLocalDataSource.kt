package com.iesam.rememora.features.audio.data.local

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.LocalModule.TIME_CACHE
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.audio.domain.Audio
import javax.inject.Inject

class AudioLocalDataSource @Inject constructor (private val audioDao: AudioDao) {
    suspend fun obtainAudios(): Either<ErrorApp, List<Audio>> {
        return try {
            val audios = audioDao.getAll()
            if (audios.isEmpty() || (audios[0].createdAt.plus(TIME_CACHE)) < System.currentTimeMillis()) {
                listOf<Audio>().right()
            } else {
                audios.map { audioEntity ->
                    audioEntity.toModel()
                }.right()
            }
        }catch (ex : Exception){
            ErrorApp.DataError.left()
        }
    }

    suspend fun saveAudios(audio: List<Audio>): Either<ErrorApp, Boolean> {
        return try {
            val ms = System.currentTimeMillis()
            val audiosEntity = audio.map { audio ->
                audio.toEntity(ms)
            }
            audioDao.insertAll(*audiosEntity.toTypedArray())
            true.right()
        } catch (ex: Exception) {
            ErrorApp.DataError.left()
        }
    }
}