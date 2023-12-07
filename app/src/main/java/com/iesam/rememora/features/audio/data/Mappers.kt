package com.iesam.rememora.features.audio.data

import com.iesam.rememora.features.audio.domain.Audio

fun AudioBdRemoteModel.toDomain() : Audio = Audio(this.description, this.id, this.source, this.title)