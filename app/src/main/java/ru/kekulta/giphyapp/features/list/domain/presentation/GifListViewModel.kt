package ru.kekulta.giphyapp.features.list.domain.presentation

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.pager.GifPagerFragment
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.giphyapp.shared.navigation.api.Command
import java.util.ServiceLoader

class GifListViewModel(private val gifRepository: GifRepository) : ViewModel() {

    private val _gifList = MutableLiveData<List<Gif>>()
    val gifList: LiveData<List<Gif>> = _gifList
    var recyclerState: Parcelable? = null

    fun fetchGifsByQuery(query: String) {
        viewModelScope.launch {
            _gifList.postValue(gifRepository.searchGifs(query))
        }
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