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
    private val binding: ListItemGifBinding, onClickListener: AdapterClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener { onClickListener.onClick(adapterPosition) }
    }

    fun onBind(gif: Gif) {
        Log.d(LOG_TAG, gif.url)
        val circularProgressDrawable = CircularProgressDrawable(binding.gifIv.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(binding.gifIv.context)
            .asGif()
            //.asBitmap()
            .load(gif.url)
            .override(gif.width, gif.height)
            .placeholder(circularProgressDrawable)
            .into(binding.gifIv)

    }


    companion object {
        const val LOG_TAG = "GifListViewHolder"
    }
}