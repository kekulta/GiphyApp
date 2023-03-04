package ru.kekulta.giphyapp.features.list.domain.presentation

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.list.domain.api.GifInteractor
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.pager.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.list.domain.models.GifListState
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState.Companion.ITEMS_ON_PAGE

import ru.kekulta.giphyapp.shared.data.models.Resource
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Router

class GifListViewModel(
    private val gifInteractor: GifInteractor,
    private val paginationInteractor: PaginationInteractor,
    private val router: Router
) : ViewModel() {


    private val currentQuery = MutableLiveData<String?>(null)

    private val state = MutableLiveData(GifListState.State.EMPTY)

    private val _gifListState =
        MediatorLiveData<GifListState>().apply {
            value = GifListState(currentState = GifListState.State.EMPTY)
        }
    val gifListState: LiveData<GifListState> = _gifListState

    private val gifListStateValue: GifListState
        get() = requireNotNull(_gifListState.value) {
            "State couldn't be null"
        }
    private val paginationState: PaginationState
        get() = gifListStateValue.paginationState


    init {
        _gifListState.addSource(
            paginationInteractor.observePaginationState().asLiveData(Dispatchers.Main)
        ) { _state ->
            _gifListState.value = gifListStateValue.copy(paginationState = _state)

            Log.d(LOG_TAG, "Flow observed: ${_state.gifList.size}")

            if (_state.gifList.isNotEmpty()) {
                state.postValue(GifListState.State.CONTENT)
            }
        }

        _gifListState.addSource(currentQuery) {
            _gifListState.value = gifListStateValue.copy(query = it)
        }
        _gifListState.addSource(state) {
            _gifListState.value = gifListStateValue.copy(currentState = it)
        }

        fetchGifsByQuery("cats")
    }

    private fun fetchGifsByQuery(query: String, page: Int = 1) {
        Log.d(LOG_TAG, "Fetch by")
        state.postValue(GifListState.State.LOADING)
        currentQuery.postValue(query)
        Log.d(LOG_TAG, "Loading posted")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(LOG_TAG, "Coroutine started")
            val result =
                gifInteractor.searchGifs(
                    GifSearchRequest(
                        query,
                        paginationState.itemsOnPage * (page - 1)
                    )
                )
            Log.d(LOG_TAG, "Query returned")
            result.collect { result ->
                when (result) {
                    is Resource.Success -> {


                        if (result.data.gifList.isEmpty()) {
                            state.postValue(GifListState.State.EMPTY)
                        }

                        paginationInteractor.setPaginationState(
                            PaginationState(
                                result.data.gifList,
                                0,
                                ITEMS_ON_PAGE,
                                result.data.pagination.pagesTotal,
                                result.data.pagination.currentPage
                            )
                        )

                    }
                    is Resource.Error -> {
                        state.postValue(GifListState.State.ERROR)
                    }
                }
            }
        }
    }

    fun searchInput(query: String) {
        fetchGifsByQuery(query)
    }

    fun cardClicked(adapterPosition: Int) {
        paginationInteractor.setPaginationState(paginationState.copy(currentItem = adapterPosition))

        router
            .navigate(Command.CommandForwardTo("Details", "list/details"))
    }

    fun prevPageButtonClicked() {
        fetchGifsByQuery(gifListStateValue.query ?: return, paginationState.currentPage - 1)
    }

    fun nextPageButtonClicked() {
        fetchGifsByQuery(gifListStateValue.query ?: return, paginationState.currentPage + 1)
    }

    companion object {
        const val LOG_TAG = "GifListViewModel"

        val SearchFactory = viewModelFactory {
            initializer {
                GifListViewModel(
                    MainServiceLocator.provideGifInteractor(),
                    MainServiceLocator.provideSearchInteractor(),
                    MainServiceLocator.provideRouter()
                )
            }
        }
    }
}