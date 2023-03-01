package ru.kekulta.giphyapp.features.list.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentListBinding
import ru.kekulta.giphyapp.features.list.domain.presentation.GifListViewModel
import ru.kekulta.giphyapp.features.main.MainActivity
import ru.kekulta.giphyapp.features.pager.GifPagerFragment

class GifListFragment : Fragment(R.layout.fragment_list) {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val adapter = GifListAdapter(this)
    private val viewModel: GifListViewModel by viewModels({ requireActivity() }) { GifListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prepareTransitions()
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gifRecyclerView.also { rv ->
//            rv.layoutManager =
//                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
//                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
//                }
            rv.layoutManager = GridLayoutManager(context, 2)
            rv.adapter = adapter
            rv.addItemDecoration(ListItemDecoration(10))
        }

        viewModel.gifList.observe(viewLifecycleOwner) { gifList ->
            adapter.gifList = gifList
        }
        //scrollToPosition()
    }

    override fun onResume() {
        super.onResume()
        viewModel.recyclerState?.let {
            binding.gifRecyclerView.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchGifsByQuery("cats")
    }

    override fun onPause() {
        super.onPause()
        viewModel.recyclerState = binding.gifRecyclerView.layoutManager?.onSaveInstanceState()
    }

    private fun scrollToPosition() {
        binding.gifRecyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.gifRecyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = binding.gifRecyclerView.layoutManager
                val viewAtPosition =
                    layoutManager!!.findViewByPosition(MainActivity.currentPosition)
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    binding.gifRecyclerView.post { layoutManager.scrollToPosition(MainActivity.currentPosition) }
                }
            }
        })
    }


    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)


        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    Log.d(GifPagerFragment.LOG_TAG, "Exit callback")


                    // Locate the ViewHolder for the clicked position.
                    val selectedViewHolder = binding.gifRecyclerView
                        .findViewHolderForAdapterPosition(MainActivity.currentPosition) ?: return
                    Log.d(GifPagerFragment.LOG_TAG, "SelectedViewHolder: ${selectedViewHolder}")

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.gifIv)

                    Log.d(
                        LOG_TAG,
                        "names[0]: ${names[0]}, sharedElements[names[0]]: ${sharedElements[names[0]]}"
                    )

                }
            })
    }

    companion object {
        const val LOG_TAG = "GifListFragment"
    }
}