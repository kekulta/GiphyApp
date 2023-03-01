package ru.kekulta.giphyapp.features.list.domain.presentation

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListViewModel(private val gifRepository: GifRepository) : ViewModel() {

    private val _gifList = MutableLiveData<List<Gif>>()
    val gifList: LiveData<List<Gif>> = _gifList
    var recyclerState: Parcelable? = null

    fun fetchGifsByQuery(query: String) {
        viewModelScope.launch {
            _gifList.postValue(gifRepository.searchGifs(query))
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                GifListViewModel(MainServiceLocator.provideGifRepository())
            }
        }
    }
}