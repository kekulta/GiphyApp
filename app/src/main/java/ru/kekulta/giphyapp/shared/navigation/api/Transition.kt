package ru.kekulta.giphyapp.shared.navigation.api

import android.os.Bundle

data class Transition(
    val tag: String,
    val screen: String,
    val args: Bundle?,
    val animation: Animation?
)