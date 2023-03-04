package ru.kekulta.giphyapp.features.search.data.dto

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("url") val url: String,
    @SerializedName("height") val height: String?,
    @SerializedName("width") val width: String?,
)