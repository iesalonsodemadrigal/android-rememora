package com.iesam.rememora.features.images.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("SELECT * FROM imageEntity")
    suspend fun getAll(): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg image: ImageEntity)

    @Query("DELETE FROM imageEntity")
    suspend fun deleteAll()
}