package ru.kekulta.giphyapp.shared.navigation.api

import android.os.Bundle

sealed interface Command {
    class CommandForwardTo(val tag: String, val screen: String, val args: Bundle? = null) : Command
    object CommandBack : Command
}