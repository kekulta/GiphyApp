package ru.kekulta.giphyapp.features.main.domain.presentation

import android.text.Spannable.Factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Navigator
import ru.kekulta.giphyapp.shared.navigation.api.Router

class MainViewModel(private val router: Router) : ViewModel() {

    private var initialized = false

    fun onResume(navigator: Navigator) {
        router.attachNavigator(navigator)
        if (!initialized) {
            initialized = true
            router.navigate(Command.CommandForwardTo("Initial", "search"))
        }
    }

    fun onBackPressed() {
        router.navigate(Command.CommandBack)
    }

    fun onPause() {
        router.detachNavigator()
    }

    fun onLikesClicked() {
        router.navigate(Command.CommandForwardTo("Likes", "likes"))
    }

    fun onSearchClicked() {
        router.navigate(Command.CommandForwardTo("Search", "search"))
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                MainViewModel(MainServiceLocator.provideRouter())
            }
        }
    }
}