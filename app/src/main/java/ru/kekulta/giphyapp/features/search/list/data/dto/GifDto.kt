package ru.kekulta.giphyapp.features.search.list.data.dto

import com.google.gson.annotations.SerializedName

data class GifDto(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("url") val embedUrl: String,
    @SerializedName("images") val images: Map<String, ImageDto>,
    @SerializedName("user") val userDto: UserDto?,
    @SerializedName("title") val title: String?,
    @SerializedName("alt_text") val contentDescription: String?
)