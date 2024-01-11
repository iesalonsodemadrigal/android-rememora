package com.iesam.rememora.features.audio.data.local

import com.iesam.rememora.features.audio.domain.Audio

fun AudioEntity.toModel() = Audio(this.id, this.description, this.source, this.title)
fun Audio.toEntity(ms: Long) = AudioEntity(this.id, this.title, this.description, this.source, ms)