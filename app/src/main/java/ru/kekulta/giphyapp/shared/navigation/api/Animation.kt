package ru.kekulta.giphyapp.shared.navigation.api

import androidx.annotation.AnimRes

data class Animation(
    @AnimRes val enter: Int,
    @AnimRes val exit: Int,
    @AnimRes val popEnter: Int,
    @AnimRes val popExit: Int
)