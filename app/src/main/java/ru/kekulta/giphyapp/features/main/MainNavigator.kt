package ru.kekulta.giphyapp.features.main

import android.app.Activity
import android.app.slice.Slice
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.kekulta.giphyapp.features.list.ui.GifListFragment
import ru.kekulta.giphyapp.features.pager.ui.GifPagerFragment
import ru.kekulta.giphyapp.shared.navigation.api.BackstackProvider
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Navigator
import ru.kekulta.giphyapp.shared.navigation.api.Transition

class MainNavigator(
    private val activity: Activity,
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
                    setReorderingAllowed(true)
                    replace(container, provideFragment(command.screen, command.args))
                    addToBackStack(null)
                }
            }

            is Command.CommandBack -> {
                fragmentManager.popBackStack()
                if (fragmentManager.backStackEntryCount == 1) {
                    activity.finish()
                }
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
}
