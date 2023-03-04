package ru.kekulta.giphyapp.features.search.data.dto

import com.google.gson.annotations.SerializedName

data class PaginationDto(
    @SerializedName("offset") val offset: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("count") val count: Int
)

