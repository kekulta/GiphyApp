package ru.kekulta.giphyapp.features.search.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("avatar_url") val avatar: String,
    @SerializedName("display_name") val name: String,
)