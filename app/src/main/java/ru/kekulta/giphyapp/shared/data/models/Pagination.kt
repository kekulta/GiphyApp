package ru.kekulta.giphyapp.shared.data.models

import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState.Companion.ITEMS_ON_PAGE

data class Pagination(
    val currentPage: Int,
    val pagesTotal: Int,
    val itemsOnPage: Int = ITEMS_ON_PAGE
)