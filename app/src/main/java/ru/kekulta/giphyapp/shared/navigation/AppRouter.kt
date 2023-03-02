package ru.kekulta.giphyapp.shared.navigation

import ru.kekulta.giphyapp.shared.navigation.api.BackstackProvider
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Navigator
import ru.kekulta.giphyapp.shared.navigation.api.Router
import ru.kekulta.giphyapp.shared.navigation.api.Transition

class AppRouter(initial: Transition? = null) : Router, BackstackProvider {
    private var navigator: Navigator? = null
    private val backstack: ArrayDeque<Transition> = ArrayDeque()

    init {
        initial?.let {
            backstack.addFirst(it)
        }
    }

    override fun navigate(command: Command) {
        navigator?.performCommand(command, false)
    }

    override fun attachNavigator(navigator: Navigator): BackstackProvider {
        this.navigator = navigator
        backstack.firstOrNull()?.let {
            navigator.performCommand(
                Command.CommandForwardTo(it.tag, it.screen, it.args),
                true
            )
        }
        return this
    }

    override fun detachNavigator() {
        this.navigator = null
    }

    override fun addToBackstack(transition: Transition) {
        backstack.addFirst(transition)
    }

    override fun removeFromBackstack(): Transition? {
        return backstack.removeFirst()
    }
}