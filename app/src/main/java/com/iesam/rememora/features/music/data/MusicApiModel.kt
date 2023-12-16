package com.iesam.rememora.features.music.data

import com.google.gson.annotations.SerializedName
import com.iesam.rememora.features.music.domain.Music

data class MusicDBModel(
    @SerializedName("description") val description: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("title") val title: String? = null
)

fun MusicDBModel.toModel(): Music = Music(this.description, this.source, this.title)
