package ru.kekulta.giphyapp.shared.navigation.api

interface Router {
    fun navigate(command: Command)
    fun attachNavigator(navigator: Navigator): BackstackProvider
    fun detachNavigator()
}