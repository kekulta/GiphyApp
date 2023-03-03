package ru.kekulta.giphyapp.features.list.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kekulta.giphyapp.features.list.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.list.domain.models.PaginationState

class PaginationInteractorImpl : PaginationInteractor {
    private val _paginationState = MutableStateFlow(PaginationState())
    override fun observePaginationState(): StateFlow<PaginationState> = _paginationState

    override fun setPaginationState(paginationState: PaginationState) {
        Log.d(LOG_TAG, paginationState.currentItem.toString())
        _paginationState.value = paginationState
        Log.d(LOG_TAG, _paginationState.value.currentItem.toString())
    }

    companion object {
        const val LOG_TAG = "PaginationInteractorImpl"
    }
}