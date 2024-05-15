package com.iesam.rememora.features.images.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "emotion") val emotion: Int?,
    @ColumnInfo(name = "created_at") var createdAt: Long
)