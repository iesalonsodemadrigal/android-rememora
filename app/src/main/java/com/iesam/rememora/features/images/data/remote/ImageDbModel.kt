package com.iesam.rememora.features.images.data.remote

import com.iesam.rememora.features.images.domain.Image

data class ImageDbModel(
    val description: String? = null,
    val id: String? = null,
    var source: String? = null,
    val title: String? = null
)

fun ImageDbModel.toModel(): Image =
    Image(this.id!!, this.description!!, this.source!!, this.title!!)