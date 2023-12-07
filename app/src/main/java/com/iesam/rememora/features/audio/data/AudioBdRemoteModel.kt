package com.iesam.rememora.features.audio.data

import com.google.gson.annotations.SerializedName

data class AudioBdRemoteModel(
    @SerializedName("description") val description : String,
    @SerializedName("id") val id : Int,
    @SerializedName("source") val source : String,
    @SerializedName("title") val title : String
)