package com.iesam.rememora.features.images.data.local

import com.iesam.rememora.features.images.domain.Image

fun Image.toEntity(ms: Long): ImageEntity =
    ImageEntity(this.id, this.title, this.description, this.source, reaction, ms)

fun ImageEntity.toDomain(): Image =
    Image(this.description, this.uid, this.source, this.title, reaction)