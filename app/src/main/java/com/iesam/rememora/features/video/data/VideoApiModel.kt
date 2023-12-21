package com.iesam.rememora.features.video.data

import com.iesam.rememora.features.video.domain.Video

data
class VideoApiModel(
    val description: String? = null,
    val id: String? = null,
    var source: String? = null,
    val title: String? = null
)

fun VideoApiModel.toModel(): Video = Video(this.description, this.id, this.source, this.title)