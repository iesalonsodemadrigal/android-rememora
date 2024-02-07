package com.iesam.rememora.features.video.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {
    @Query("SELECT * FROM videoentity")
    fun getAll(): List<VideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg  videos: VideoEntity)

    @Query("DELETE FROM videoentity")
    suspend fun deleteAll()
}