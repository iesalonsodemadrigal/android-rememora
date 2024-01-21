package com.iesam.rememora.features.music.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MusicDao {
    @Query("SELECT * FROM musicentity")
    suspend fun loadAll(): List<MusicEntity>

    @Insert()
    suspend fun insertAll(vararg users: MusicEntity)


    @Query("DELETE FROM musicentity")
    suspend fun deleteAll()
}