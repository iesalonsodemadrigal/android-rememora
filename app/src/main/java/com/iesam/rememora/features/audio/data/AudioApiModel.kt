package com.iesam.rememora.features.audio.data

import com.google.gson.annotations.SerializedName
import com.iesam.rememora.features.audio.domain.Audio

data class AudioDbModel (
    @SerializedName("description") val description: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("title") val title: String? = null
)

fun AudioDbModel.toModel() : Audio = Audio(this.description, this.source, this.title)