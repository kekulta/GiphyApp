package ru.kekulta.giphyapp.features.pager.domain.models

import ru.kekulta.giphyapp.features.list.domain.models.PaginationState

data class GifPagerState (
    val paginationState: PaginationState = PaginationState()
    )