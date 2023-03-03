package ru.kekulta.giphyapp.features.list.domain.models

import ru.kekulta.giphyapp.shared.data.models.Gif

data class PaginationState(
    val gifList: List<Gif> = emptyList(),
    val pagesTotal: Int = 0,
    val currentPage: Int = 0
)