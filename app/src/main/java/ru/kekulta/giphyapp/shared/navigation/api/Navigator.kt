package ru.kekulta.giphyapp.shared.navigation.api

interface Navigator {
    fun performCommand(command: Command, noAnimation: Boolean = false)
}