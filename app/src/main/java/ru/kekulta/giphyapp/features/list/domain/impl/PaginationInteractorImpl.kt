package ru.kekulta.giphyapp.features.list.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kekulta.giphyapp.features.list.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.list.domain.models.PaginationState

class PaginationInteractorImpl : PaginationInteractor {
    private val _paginationState = MutableStateFlow(PaginationState())
    override fun observePaginationState(): Flow<PaginationState> = _paginationState

    override fun setPaginationState(paginationState: PaginationState) {
        _paginationState.value = paginationState
    }


}