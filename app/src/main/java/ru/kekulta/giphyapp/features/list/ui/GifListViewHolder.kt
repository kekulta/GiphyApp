package ru.kekulta.giphyapp.features.list.ui

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ListItemGifBinding
import ru.kekulta.giphyapp.features.main.MainActivity
import ru.kekulta.giphyapp.features.pager.GifPagerFragment
import ru.kekulta.giphyapp.shared.data.models.Gif
import java.util.concurrent.atomic.AtomicBoolean

class GifListViewHolder(
    private val fragment: Fragment,
    private val list: List<Gif>,
    private val binding: ListItemGifBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.gifIv.setOnClickListener { _ ->
            // Update the position.
            MainActivity.currentPosition = adapterPosition

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).

            (fragment.exitTransition as TransitionSet?)!!.excludeTarget(binding.gifIv, true)

            val transitioningView = binding.gifIv

            fragment.parentFragmentManager.commit {
//                setReorderingAllowed(true)
                addSharedElement(transitioningView, transitioningView.transitionName + "hero")

                replace(
                    R.id.container, GifPagerFragment.newInstance(list), GifPagerFragment::class.java
                        .simpleName
                )
                addToBackStack(null)
            }
        }
    }

    private val enterTransitionStarted: AtomicBoolean = AtomicBoolean()

    fun onLoadCompleted() {
        // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
        if (MainActivity.currentPosition != adapterPosition) {
            return
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return
        }
        fragment.startPostponedEnterTransition()
    }

    fun onBind(gif: Gif) {

        binding.gifIv.transitionName = list[adapterPosition].id
        Log.d(LOG_TAG, gif.url)
        val circularProgressDrawable = CircularProgressDrawable(binding.gifIv.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()



        Glide.with(binding.gifIv.context)
            .asGif()
            .load(gif.url)
            .override(gif.width, gif.height)
            .placeholder(circularProgressDrawable)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any,
                    target: Target<GifDrawable?>, isFirstResource: Boolean
                ): Boolean {
                    this@GifListViewHolder.onLoadCompleted()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    this@GifListViewHolder.onLoadCompleted()
                    return false
                }
            })
            .into(binding.gifIv)
    }


    companion object {
        const val LOG_TAG = "GifListViewHolder"
    }
}