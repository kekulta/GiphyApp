package ru.kekulta.giphyapp.features.list.domain.models

import retrofit2.http.Query
import ru.kekulta.giphyapp.shared.data.models.Gif

data class GifListState(
    val query: String? = null,
    val paginationState: PaginationState = PaginationState(),
    val currentState: State
) {

    enum class State {
        CONTENT, EMPTY, LOADING, ERROR
    }

}