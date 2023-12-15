package com.iesam.rememora.features.video.data

import com.google.gson.annotations.SerializedName
import com.iesam.rememora.features.video.domain.Video

data
class VideoApiModel(
    @SerializedName("description") val description: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("title") val title: String? = null
)

fun VideoApiModel.toModel(): Video = Video(this.description, this.source, this.title)