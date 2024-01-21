package com.iesam.rememora.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iesam.rememora.features.audio.data.local.AudioDao
import com.iesam.rememora.features.audio.data.local.AudioEntity
import com.iesam.rememora.features.images.data.local.ImageDao
import com.iesam.rememora.features.images.data.local.ImageEntity
import com.iesam.rememora.features.music.data.local.MusicDao
import com.iesam.rememora.features.music.data.local.MusicEntity
import com.iesam.rememora.features.video.data.local.VideoEntity

@Database(
    entities = [ImageEntity::class, MusicEntity::class, AudioEntity::class, VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun audioDao(): AudioDao
    abstract fun imageDao(): ImageDao
}