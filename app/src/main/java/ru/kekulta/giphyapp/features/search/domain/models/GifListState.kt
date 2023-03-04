package ru.kekulta.giphyapp.features.search.domain.models

import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState

data class GifListState(
    val query: String? = null,
    val paginationState: PaginationState = PaginationState(),
    val currentState: State
) {

    enum class State {
        CONTENT, EMPTY, LOADING, ERROR
    }

}