package com.iesam.rememora.features.music.data.local

import com.iesam.rememora.features.music.domain.Music

fun Music.toEntity(time: Long): MusicEntity =
    MusicEntity(this.id, this.title, this.description, this.source, time)

fun List<Music>.toEntity(time: Long): List<MusicEntity> = this.map {
    it.toEntity(time)
}

fun MusicEntity.toDomain(): Music =
    Music(this.id, this.description, this.source, this.title)

fun List<MusicEntity>.toDomain(): List<Music> = this.map {
    it.toDomain()
}