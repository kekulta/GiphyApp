package ru.kekulta.giphyapp.features.main.domain

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.kekulta.giphyapp.features.likes.domain.presentation.GifLikesListViewModel
import ru.kekulta.giphyapp.features.likes.ui.GifLikesListFragment
import ru.kekulta.giphyapp.features.pager.domain.presentation.GifPagerViewModel
import ru.kekulta.giphyapp.features.search.ui.SearchListFragment
import ru.kekulta.giphyapp.features.pager.ui.GifPagerFragment

import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Navigator

class MainNavigator(
    private val activity: Activity,
    private val fragmentManager: FragmentManager,
    @IdRes private val container: Int
) : Navigator {
    private var backstack: ArrayDeque<String> = ArrayDeque()

    override fun performCommand(
        command: Command,
        noAnimation: Boolean,
    ) {
        when (command) {
            is Command.CommandForwardTo -> {
                if (backstack.firstOrNull() != command.destination) {
                    backstack.addFirst(command.destination)
                    fragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(container, provideFragment(command.destination, command.args))
                        addToBackStack(null)
                    }
                }
            }

            is Command.CommandBack -> {
                fragmentManager.popBackStack()
                backstack.removeFirstOrNull()
                if (fragmentManager.backStackEntryCount == 1) {
                    activity.finish()
                }
            }
        }
    }

    private fun provideFragment(destination: String, args: Bundle?): Fragment {
        return when (destination) {
            "search/details" -> GifPagerFragment.newInstance((args ?: Bundle()).apply {
                putString(GifPagerFragment.FACTORY_PRODUCER, GifPagerViewModel.SEARCH_KEY)
            })
            "search" -> SearchListFragment()
            "likes/details" -> GifPagerFragment.newInstance((args ?: Bundle()).apply {
                putString(GifPagerFragment.FACTORY_PRODUCER, GifPagerViewModel.LIKES_KEY)
            })
            "likes" -> GifLikesListFragment()
            else -> throw IllegalArgumentException("Invalid screen")
        }
    }
}
