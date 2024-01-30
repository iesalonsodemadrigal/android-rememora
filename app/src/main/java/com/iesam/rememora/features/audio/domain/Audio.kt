package com.iesam.rememora.features.audio.domain

data class Audio(
    val id: String,
    val description: String,
    var source: String,
    val title: String
)