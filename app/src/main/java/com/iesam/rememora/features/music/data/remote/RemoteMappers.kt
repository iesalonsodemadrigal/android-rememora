package com.iesam.rememora.features.music.data.remote

import com.iesam.rememora.features.music.domain.Song

fun MusicDBModel.toModel(): Song =
    Song(this.id!!, this.description!!, this.source!!, this.title!!)