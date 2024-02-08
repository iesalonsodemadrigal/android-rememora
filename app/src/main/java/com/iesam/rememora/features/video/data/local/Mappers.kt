package com.iesam.rememora.features.video.data.local

import com.iesam.rememora.features.video.domain.Video

fun Video.toEntity(time: Long): VideoEntity =
    VideoEntity(this.id, this.title, this.description, this.source, time)

fun VideoEntity.toModel(): Video =
    Video(this.description, this.id, this.source, this.title)