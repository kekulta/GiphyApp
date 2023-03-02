package ru.kekulta.giphyapp.features.list.ui

import android.os.Bundle
import android.transition.TransitionInflater
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
import ru.kekulta.giphyapp.features.list.domain.presentation.GifListViewModel

class GifListFragment : Fragment(R.layout.fragment_list) {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: GifListViewModel by viewModels({ requireActivity() }) { GifListViewModel.Factory }
    private val adapter = GifListAdapter(this).apply {
        setAdapterClickListener {
            viewModel.cardClicked(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // TODO do something with animations
        val animationInflater = TransitionInflater.from(context)
        enterTransition =
            animationInflater.inflateTransition(R.transition.fade).addTarget(binding.root)
        exitTransition = animationInflater.inflateTransition(R.transition.fade)
        reenterTransition =
            animationInflater.inflateTransition(R.transition.fade).addTarget(binding.root)

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

        viewModel.gifList.observe(viewLifecycleOwner) { gifList ->
            adapter.gifList = gifList
        }
    }


    companion object {
        const val LOG_TAG = "GifListFragment"
    }
}