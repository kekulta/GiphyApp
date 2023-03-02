package ru.kekulta.giphyapp.features.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.kekulta.giphyapp.features.list.ui.GifListFragment
import ru.kekulta.giphyapp.features.pager.GifPagerFragment
import ru.kekulta.giphyapp.shared.navigation.api.BackstackProvider
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Navigator
import ru.kekulta.giphyapp.shared.navigation.api.Transition

class MainNavigator(
    private val fragmentManager: FragmentManager,
    @IdRes private val container: Int
) : Navigator {
    override fun performCommand(
        command: Command,
        noAnimation: Boolean,
    ) {
        when (command) {
            is Command.CommandForwardTo -> {
                fragmentManager.commit {
                    if (!noAnimation) {
                        provideAnimation(command.screen)?.let {
                            setCustomAnimations(it.enter, it.exit, it.popEnter, it.popExit)
                        }
                    }
                    replace(container, provideFragment(command.screen, command.args))
                    addToBackStack(null)
                }
            }

            is Command.CommandBack -> {
                fragmentManager.popBackStack()

            }
        }
    }

    private fun provideFragment(screen: String, args: Bundle?): Fragment {
        return when (screen) {
            "list/details" -> GifPagerFragment.newInstance(args)
            "list" -> GifListFragment()
            else -> throw IllegalArgumentException("Invalid screen")
        }
    }

    private fun provideAnimation(screen: String): Animation? {
        return when (screen) {
            "list/details" -> Animation(
                androidx.appcompat.R.anim.abc_slide_in_bottom,
                androidx.appcompat.R.anim.abc_fade_out,
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_slide_out_bottom
            )

            else -> null
        }
    }
}
