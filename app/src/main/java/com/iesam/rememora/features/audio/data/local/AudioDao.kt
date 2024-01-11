package com.iesam.rememora.features.audio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AudioDao {
    @Query("SELECT * FROM audioEntity")
    suspend fun getAll(): List<AudioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: AudioEntity)

    @Query("DELETE FROM audioEntity")
    suspend fun deleteAll()
}