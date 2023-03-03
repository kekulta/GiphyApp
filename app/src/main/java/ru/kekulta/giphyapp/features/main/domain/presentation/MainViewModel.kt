package ru.kekulta.giphyapp.features.main.domain.presentation

import androidx.lifecycle.ViewModel
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.shared.navigation.api.Command

class MainViewModel : ViewModel() {

    private var initialized = false

    fun onResume() {
        if (!initialized) {
            initialized = true
            MainServiceLocator.getRouter().navigate(Command.CommandForwardTo("Initial", "list"))
        }
    }
}