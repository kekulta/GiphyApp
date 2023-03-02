package ru.kekulta.giphyapp.shared.navigation.api

import android.os.Bundle
import ru.kekulta.giphyapp.features.main.Animation

data class Transition(
    val tag: String,
    val screen: String,
    val args: Bundle?,
    val animation: Animation?
)