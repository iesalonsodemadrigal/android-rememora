package com.iesam.rememora.features.music.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MusicDao {
    @Query("SELECT * FROM musicentity")
    suspend fun loadAll(): List<MusicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: MusicEntity)


    @Query("DELETE FROM musicentity")
    suspend fun deleteAll()
}