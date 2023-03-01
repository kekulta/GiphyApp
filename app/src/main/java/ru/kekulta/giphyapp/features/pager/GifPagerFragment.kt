package ru.kekulta.giphyapp.features.pager

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentPagerGifBinding
import ru.kekulta.giphyapp.features.main.MainActivity
import ru.kekulta.giphyapp.shared.data.models.Gif

private const val ARG_ITEMS = "arg_items"

class GifPagerFragment : Fragment(R.layout.fragment_pager_gif) {
    private val binding: FragmentPagerGifBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val adapter by lazy {
        GifPagerAdapter(this).apply {
            items = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelableArray(ARG_ITEMS, Gif::class.java)?.toList()
                    ?: emptyList()
            } else {
                arguments?.getParcelableArray(ARG_ITEMS)?.map { it as Gif } ?: emptyList()
            }
        }
    }
    private val viewPager by lazy { binding.root }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewPager.adapter = adapter
        viewPager.currentItem = MainActivity.currentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                MainActivity.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        if (savedInstanceState == null) postponeEnterTransition()

        return viewPager
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    Log.d(LOG_TAG, "Enter callback")
                    val currentFragment = viewPager.adapter
                        ?.instantiateItem(viewPager, MainActivity.currentPosition) as Fragment
                    Log.d(LOG_TAG, "Current fragment: ${currentFragment}")
                    val view = currentFragment.view ?: return

                    sharedElements[names[0]] = view.findViewById(R.id.image)
                    Log.d(LOG_TAG, "names[0]: ${names[0]}, sharedElements[names[0]]: ${sharedElements[names[0]]}")
                }
            })
    }

    companion object {
        const val LOG_TAG = "GifPagerFragment"

        @JvmStatic
        fun newInstance(gifList: List<Gif>) =
            GifPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_ITEMS, gifList.toTypedArray())
                }
            }
    }
}