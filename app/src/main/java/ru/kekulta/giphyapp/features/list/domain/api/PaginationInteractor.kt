package ru.kekulta.giphyapp.features.list.domain.api


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kekulta.giphyapp.features.list.domain.models.PaginationState


interface PaginationInteractor {
    fun observePaginationState(): Flow<PaginationState>
    fun setPaginationState(paginationState: PaginationState)
}