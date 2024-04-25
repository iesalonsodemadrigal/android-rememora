package com.iesam.rememora.features.images.data.remote

import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.SaveImageUseCase

data class DownloadImageDbModel(
    val description: String? = null,
    val id: String? = null,
    var source: String? = null,
    val title: String? = null,
    val reaction: Int? = null
)

data class UploadImageDbModel(
    val description: String,
    val id: String,
    var source: String,
    val title: String,
    val reaction: Int?
)

fun DownloadImageDbModel.toModel(): Image =
    Image(this.description!!, this.id!!, this.source!!, this.title!!, this.reaction)

fun SaveImageUseCase.Input.toRemote(): UploadImageDbModel =
    UploadImageDbModel(this.description, this.id, this.image, this.title, this.reaction)