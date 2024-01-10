package com.iesam.rememora.features.music.data.remote

import com.iesam.rememora.features.music.domain.Music

fun MusicDBModel.toModel(): Music =
    Music(this.id, this.description, this.source, this.title)