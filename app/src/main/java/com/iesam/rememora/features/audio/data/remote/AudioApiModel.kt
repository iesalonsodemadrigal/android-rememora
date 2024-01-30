package com.iesam.rememora.features.audio.data.remote

import com.iesam.rememora.features.audio.domain.Audio

data class AudioDbModel(
    val description: String? = null,
    val id: String? = null,
    var source: String? = null,
    val title: String? = null
)

fun AudioDbModel.toModel(): Audio =
    Audio(this.id!!, this.description!!, this.source!!, this.title!!)