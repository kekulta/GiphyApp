package ru.kekulta.giphyapp.shared.navigation.api

interface BackstackProvider {
    fun removeFromBackstack(): Transition?
    fun addToBackstack(transition: Transition)
}