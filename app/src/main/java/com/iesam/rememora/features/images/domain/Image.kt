package com.iesam.rememora.features.images.domain

data class Image(
    val description: String,
    val id: String,
    var source: String,
    val title: String,
    var reaction: Int?
)