package ru.kekulta.giphyapp.features.pager.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentPagerGifBinding
import ru.kekulta.giphyapp.di.MainServiceLocator
import ru.kekulta.giphyapp.features.pager.domain.presentation.GifPagerViewModel
import ru.kekulta.giphyapp.shared.navigation.api.Command

class
GifPagerFragment :
    Fragment(R.layout.fragment_pager_gif) {
    private val binding: FragmentPagerGifBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val adapter by lazy { GifPagerAdapter(this) }
    private val viewPager by lazy { binding.viewPager }
    private val viewModel: GifPagerViewModel by viewModels({ requireActivity() }) { GifPagerViewModel.SearchFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewPager.adapter = adapter
        viewPager.currentItem = arguments?.getInt(INITIAL_ITEM) ?: 0

        viewModel.gifPagerState.observe(viewLifecycleOwner) { state ->

            adapter.items = state.paginationState.gifList
            Log.d(LOG_TAG, "observed : ${state.paginationState.currentItem}")
            viewPager.currentItem = state.paginationState.currentItem
            binding.counter.text = resources.getString(
                R.string.page_counter_format,
                (state.paginationState.currentItem + 1),
                state.paginationState.gifList.size
            )
        }



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.d(LOG_TAG, position.toString())
                viewModel.pageChanged(position)
            }
        })

        val transitionInflater = TransitionInflater.from(context)
        enterTransition =
            transitionInflater.inflateTransition(R.transition.slide_bottom).addTarget(binding.root)
        returnTransition = transitionInflater.inflateTransition(R.transition.slide_bottom)

        binding.backButton.setOnClickListener {
            MainServiceLocator.provideRouter().navigate(Command.CommandBack)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.counter.text = resources.getString(
            R.string.page_counter_format,
            (arguments?.getInt(INITIAL_ITEM) ?: 0) + 1,
            adapter.items.size
        )
        // TODO Полезно но не нужно, убрать
//        val ta =
//            requireContext().theme.obtainStyledAttributes(R.styleable.ThemeColors)
//        val c = ta.getColor(R.styleable.ThemeColors_colorSurfaceVariant, 1000)
//
//        requireActivity().window.statusBarColor = c

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