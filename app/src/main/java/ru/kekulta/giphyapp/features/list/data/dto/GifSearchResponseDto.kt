package ru.kekulta.giphyapp.features.list.data.dto

import com.google.gson.annotations.SerializedName

data class GifSearchResponseDto(
    @SerializedName("data") val data: List<GifDto>,
    @SerializedName("pagination") val pagination: PaginationDto
) : Response()
