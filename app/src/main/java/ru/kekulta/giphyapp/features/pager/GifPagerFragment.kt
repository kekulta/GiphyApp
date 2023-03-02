package ru.kekulta.giphyapp.features.pager

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentPagerGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif


class GifPagerFragment : Fragment(R.layout.fragment_pager_gif) {
    private val binding: FragmentPagerGifBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val adapter by lazy {
        GifPagerAdapter(this).apply {
            items = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelableArray(GIF_LIST, Gif::class.java)?.toList() ?: emptyList()
            } else {
                arguments?.getParcelableArray(GIF_LIST)?.map { it as Gif } ?: emptyList()
            }
        }
    }
    private val viewPager by lazy { binding.root }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewPager.adapter = adapter
        viewPager.currentItem = arguments?.getInt(INITIAL_ITEM) ?: 0

        val inflater = TransitionInflater.from(context)
        enterTransition =
            inflater.inflateTransition(R.transition.slide_bottom).addTarget(binding.root)
        returnTransition = inflater.inflateTransition(R.transition.slide_bottom)

        return viewPager
    }

    companion object {
        const val LOG_TAG = "GifPagerFragment"
        const val GIF_LIST = "arg_items"
        const val INITIAL_ITEM = "arg_initial"

        @JvmStatic
        fun newInstance(bundle: Bundle?) = GifPagerFragment().apply {
            arguments = bundle
        }
    }
}