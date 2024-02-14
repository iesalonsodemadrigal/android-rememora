package com.iesam.rememora.features.music.data.local

import com.iesam.rememora.features.music.domain.Song

fun Song.toEntity(time: Long): MusicEntity =
    MusicEntity(this.id, this.title, this.description, this.source, time)

fun List<Song>.toEntity(time: Long): List<MusicEntity> = this.map {
    it.toEntity(time)
}

fun MusicEntity.toDomain(): Song =
    Song(this.id, this.description, this.source, this.title)

fun List<MusicEntity>.toDomain(): List<Song> = this.map {
    it.toDomain()
}