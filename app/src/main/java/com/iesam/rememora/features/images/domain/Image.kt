package com.iesam.rememora.features.images.domain

data class Image(
    val description: String? = null,
    val id: Int = 0,
    var source: String? = null,
    val title: String? = null
)