package ru.kekulta.giphyapp.features.list.domain.presentation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.list.domain.models.GifListState
import ru.kekulta.giphyapp.features.pager.ui.GifPagerFragment
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.giphyapp.shared.data.models.Resource
import ru.kekulta.giphyapp.shared.navigation.api.Command

class GifListViewModel(private val gifRepository: GifRepository) : ViewModel() {

    private val gifList = MutableLiveData<List<Gif>>(emptyList())
    private val currentQuery = MutableLiveData<String?>(null)
    private val currentPage = MutableLiveData<Int>(0)
    private val pagesTotal = MutableLiveData<Int>(0)
    private val state = MutableLiveData(GifListState.State.EMPTY)

    private val _gifListState =
        MediatorLiveData<GifListState>().apply {
            value = GifListState(currentState = GifListState.State.EMPTY)
        }
    val gifListState: LiveData<GifListState> = _gifListState


    init {
        _gifListState.addSource(gifList) {
            _gifListState.value = requireNotNull(_gifListState.value?.copy(gifList = it)) {
                "State couldn't be null"
            }
        }
        _gifListState.addSource(currentQuery) {
            _gifListState.value = requireNotNull(_gifListState.value?.copy(query = it)) {
                "State couldn't be null"
            }
        }
        _gifListState.addSource(currentPage) {
            _gifListState.value = requireNotNull(_gifListState.value?.copy(currentPage = it)) {
                "State couldn't be null"
            }
        }
        _gifListState.addSource(pagesTotal) {
            _gifListState.value = requireNotNull(_gifListState.value?.copy(pagesTotal = it)) {
                "State couldn't be null"
            }
        }
        _gifListState.addSource(state) {
            _gifListState.value = requireNotNull(_gifListState.value?.copy(currentState = it)) {
                "State couldn't be null"
            }
        }
        fetchGifsByQuery("cats")
    }

    private fun fetchGifsByQuery(query: String, offset: Int = 0) {
        state.postValue(GifListState.State.LOADING)
        currentQuery.postValue(query)

        viewModelScope.launch {
            val result = gifRepository.searchGifs(GifSearchRequest(query, offset))
            when (result) {
                is Resource.Success -> {
                    gifList.postValue(result.data.gifList)
                    if (result.data.gifList.isEmpty()) {
                        state.postValue(GifListState.State.EMPTY)
                    } else {
                        state.postValue(GifListState.State.CONTENT)
                    }
                }

                is Resource.Error -> {
                    state.postValue(GifListState.State.ERROR)
                }
            }

        }
    }

    fun searchInput(query: String) {
        fetchGifsByQuery(query)
    }

    fun cardClicked(adapterPosition: Int) {
        Log.d(LOG_TAG, gifList.value.toString())

        MainServiceLocator.getRouter()
            .navigate(Command.CommandForwardTo("Details", "list/details", Bundle().apply {
                putParcelableArray(GifPagerFragment.GIF_LIST, gifList.value?.toTypedArray())
                putInt(GifPagerFragment.INITIAL_ITEM, adapterPosition)
            }))
    }

    companion object {
        const val LOG_TAG = "GifListViewModel"

        val Factory = viewModelFactory {
            initializer {
                GifListViewModel(MainServiceLocator.provideGifRepository())
            }
        }
    }
}