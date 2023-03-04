package ru.kekulta.giphyapp.features.pager.domain.api


import kotlinx.coroutines.flow.StateFlow
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState


interface PaginationInteractor {
    fun observePaginationState(): StateFlow<PaginationState>
    fun setPaginationState(paginationState: PaginationState)
}