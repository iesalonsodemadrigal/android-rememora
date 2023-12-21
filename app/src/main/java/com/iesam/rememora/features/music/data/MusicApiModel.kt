package com.iesam.rememora.features.music.data

import com.iesam.rememora.features.music.domain.Music

data class MusicDBModel(
    val description: String? = null,
    val id: String? = null,
    var source: String? = null,
    val title: String? = null
)

fun MusicDBModel.toModel(): Music =
    Music(this.id, this.description, this.source, this.title)
