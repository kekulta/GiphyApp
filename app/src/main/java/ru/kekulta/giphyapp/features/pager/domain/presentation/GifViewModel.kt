package ru.kekulta.giphyapp.features.pager.domain.presentation

import android.text.Spannable.Factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.likes.domain.api.LikesRepository

class GifViewModel(private val likesRepository: LikesRepository) : ViewModel() {
    fun changeLike(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (likesRepository.isLiked(id)) {
                likesRepository.deleteById(id)
            } else {
                likesRepository.insert(id)
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                GifViewModel(MainServiceLocator.provideLikesRepository())
            }
        }
    }
}