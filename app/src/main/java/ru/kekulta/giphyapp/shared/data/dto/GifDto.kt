package ru.kekulta.giphyapp.shared.data.dto

import com.google.gson.annotations.SerializedName

data class GifDto(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("url") val embedUrl: String,
    @SerializedName("images") val images: Map<String, ImageDto>
)