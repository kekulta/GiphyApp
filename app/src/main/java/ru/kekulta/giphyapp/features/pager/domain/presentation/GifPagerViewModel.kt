package ru.kekulta.giphyapp.features.pager.domain.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.pager.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.pager.domain.models.GifPagerState
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Router

class GifPagerViewModel(private val paginationInteractor: PaginationInteractor, router: Router) :
    ViewModel() {

    private val _gifPagerState = MediatorLiveData<GifPagerState>()
    val gifPagerState: LiveData<GifPagerState> = _gifPagerState

    private val gifPagerStateValue: GifPagerState
        get() = _gifPagerState.value ?: GifPagerState()

    init {
        _gifPagerState.addSource(
            paginationInteractor.observePaginationState().asLiveData()
        ) { state ->
            Log.d(LOG_TAG, "Get from Flow: ${state.currentItem}")
            _gifPagerState.value = gifPagerStateValue.copy(paginationState = state)
            if (state.gifList.isEmpty()) {
                router.navigate(Command.CommandBack)
            }
        }
    }

    fun pageChanged(item: Int) {
        paginationInteractor.setPaginationState(gifPagerStateValue.paginationState.copy(currentItem = item))
    }

    companion object {
        const val LOG_TAG = "GifPagerViewModel"
        const val SEARCH_KEY = "SEARCH_FACTORY_PRODUCER"
        const val LIKES_KEY = "LIKES_FACTORY_PRODUCER"
        val SearchFactory = viewModelFactory {
            initializer {
                GifPagerViewModel(
                    MainServiceLocator.provideSearchInteractor(),
                    MainServiceLocator.provideRouter()
                )
            }
        }
        val LikesFactory = viewModelFactory {
            initializer {
                GifPagerViewModel(
                    MainServiceLocator.provideLikesInteractor(),
                    MainServiceLocator.provideRouter()
                )
            }
        }
    }
}