package ru.kekulta.giphyapp.features.likes.ui


import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentListLikesBinding
import ru.kekulta.giphyapp.features.likes.domain.presentation.GifLikesListViewModel
import ru.kekulta.giphyapp.features.search.domain.models.GifListState
import ru.kekulta.giphyapp.features.search.ui.AdapterClickListener
import ru.kekulta.giphyapp.features.search.ui.GifListAdapter
import ru.kekulta.giphyapp.features.search.ui.ListItemDecoration


class GifLikesListFragment : Fragment(R.layout.fragment_list_likes) {

    private val binding: FragmentListLikesBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: GifLikesListViewModel by viewModels({ requireActivity() }) { GifLikesListViewModel.Factory }
    private val adapter = GifListAdapter().apply {
        setAdapterClickListener(object : AdapterClickListener {
            override fun onClick(adapterPosition: Int) {
                viewModel.cardClicked(adapterPosition)
            }

            override fun onLikeClick(adapterPosition: Int) {
                viewModel.cardBookmarkClicked(adapterPosition)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // TODO do something with animations
        val animationInflater = TransitionInflater.from(context)
//        enterTransition =
//            animationInflater.inflateTransition(R.transition.fade).addTarget(binding.root)
//        exitTransition = animationInflater.inflateTransition(R.transition.fade)
//        reenterTransition =
//            animationInflater.inflateTransition(R.transition.fade).addTarget(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gifRecyclerView.also { rv ->
            rv.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                }
            rv.adapter = adapter
            rv.addItemDecoration(ListItemDecoration(10))
        }


        viewModel.gifListState.observe(viewLifecycleOwner) { currentState ->
            Log.d(LOG_TAG, "State observed: ${currentState.currentState}")
            when (currentState.currentState) {
                GifListState.State.CONTENT -> {
                    adapter.gifList = currentState.paginationState.gifList
                    binding.gifRecyclerView.visibility = View.VISIBLE
                    binding.backButton.visibility = View.VISIBLE
                    binding.forwardButton.visibility = View.VISIBLE
                    binding.pageCounter.visibility = View.VISIBLE
                    binding.info.visibility = View.INVISIBLE
                    binding.pageCounter.text = getString(
                        R.string.page_counter_format,
                        currentState.paginationState.currentPage,
                        currentState.paginationState.pagesTotal - 1
                    )
                    binding.backButton.isEnabled = currentState.paginationState.currentPage != 1
                    binding.forwardButton.isEnabled =
                        currentState.paginationState.currentPage != currentState.paginationState.pagesTotal

                    binding.backButton.setOnClickListener {
                        viewModel.prevPageButtonClicked()
                    }
                    binding.forwardButton.setOnClickListener {
                        viewModel.nextPageButtonClicked()
                    }

                }

                GifListState.State.EMPTY, GifListState.State.LOADING, GifListState.State.ERROR -> {
                    binding.gifRecyclerView.visibility = View.GONE
                    binding.backButton.visibility = View.GONE
                    binding.forwardButton.visibility = View.GONE
                    binding.pageCounter.visibility = View.GONE
                    binding.info.visibility = View.VISIBLE

                    binding.info.text = when (currentState.currentState) {
                        GifListState.State.EMPTY -> "EMPTY"
                        GifListState.State.LOADING -> "LOADING"
                        GifListState.State.ERROR -> "ERROR"
                        GifListState.State.CONTENT -> "HERE SHOULD BE CONTENT BUT SOMETHING WENT WRONG"
                    }
                }
            }
        }

    }


    companion object {
        const val LOG_TAG = "GifLikesListFragment"
    }
}