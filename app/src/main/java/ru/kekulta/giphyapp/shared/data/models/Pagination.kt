package ru.kekulta.giphyapp.shared.data.models

data class Pagination(
    val offset: Int,
    val totalCount: Int,
    val count: Int
)