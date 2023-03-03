package ru.kekulta.giphyapp.features.list.domain.models

import ru.kekulta.giphyapp.shared.data.models.Gif

data class PaginationState(
    val gifList: List<Gif> = emptyList(),
    val currentItem: Int = 0,
    val itemsOnPage: Int = ITEMS_ON_PAGE,
    val pagesTotal: Int = 0,
    val currentPage: Int = 0
) {
    companion object {
        const val ITEMS_ON_PAGE = 25
    }
}