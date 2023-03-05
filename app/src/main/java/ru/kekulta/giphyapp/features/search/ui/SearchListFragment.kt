package ru.kekulta.giphyapp.features.search.ui

import android.os.Bundle
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
import ru.kekulta.giphyapp.databinding.FragmentListBinding
import ru.kekulta.giphyapp.features.search.domain.models.GifListState
import ru.kekulta.giphyapp.features.search.domain.presentation.GifListViewModel


class SearchListFragment : Fragment(R.layout.fragment_list) {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: GifListViewModel by viewModels({ requireActivity() }) { GifListViewModel.Factory }
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

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.text = binding.searchView.text
            binding.searchView.hide()
            viewModel.searchInput(binding.searchView.text.toString())
            false
        }



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
            Log.d(LOG_TAG, """
                |State observed: ${currentState.currentState}
                |currentPage: ${currentState.paginationState.currentPage}
                |pagesTotal: ${currentState.paginationState.pagesTotal}
                |currentItem: ${currentState.paginationState.currentItem}
            """.trimMargin())
            when (currentState.currentState) {
                GifListState.State.CONTENT -> {
                    adapter.submitList(currentState.paginationState.gifList)
                    binding.gifRecyclerView.visibility = View.VISIBLE
                    binding.backButton.visibility = View.VISIBLE
                    binding.forwardButton.visibility = View.VISIBLE
                    binding.pageCounter.visibility = View.VISIBLE
                    binding.info.visibility = View.INVISIBLE
                    binding.searchBar.text = currentState.query ?: ""
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
                    binding.searchBar.text = currentState.query ?: ""

                    binding.info.text = when (currentState.currentState) {
                        GifListState.State.EMPTY -> "EMPTY"
                        GifListState.State.LOADING -> "LOADING"
                        // Strange warning
                        GifListState.State.ERROR -> "ERROR"
                        GifListState.State.CONTENT -> "HERE SHOULD BE CONTENT BUT SOMETHING WENT WRONG"
                    }
                }
            }
        }

    }


    companion object {
        const val LOG_TAG = "GifListFragment"
    }
}