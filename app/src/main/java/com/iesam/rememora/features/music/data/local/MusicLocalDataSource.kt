package com.iesam.rememora.features.music.data.local

import com.iesam.rememora.app.Either
import com.iesam.rememora.app.di.LocalModule.TIME_CACHE
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import com.iesam.rememora.features.music.domain.Music
import javax.inject.Inject

class MusicLocalDataSource @Inject constructor(private val musicDao: MusicDao) {
    suspend fun saveMusic(musicList: List<Music>): Either<ErrorApp, Boolean> {
        return try {
            val musicEntities = musicList.toEntity(System.currentTimeMillis())
            musicDao.insertAll(*musicEntities.toTypedArray())
            true.right()
        } catch (ex: Exception) {
            ErrorApp.DataError.left()
        }
    }

    suspend fun getAllMusic(): Either<ErrorApp, List<Music>> {
        return try {
            val localMusic = musicDao.loadAll()
            if (localMusic.isEmpty() || localMusic.first().createdAt.plus(TIME_CACHE) < System.currentTimeMillis()) {
                listOf<Music>().right()
            } else {
                localMusic.toDomain().right()
            }
        } catch (ex: Exception) {
            ErrorApp.DataError.left()
        }
    }

    suspend fun deleteAllMusic(): Either<ErrorApp, Boolean> {
        return try {
            musicDao.deleteAll()
            true.right()
        } catch (ex: Exception) {
            ErrorApp.DataError.left()
        }
    }

}