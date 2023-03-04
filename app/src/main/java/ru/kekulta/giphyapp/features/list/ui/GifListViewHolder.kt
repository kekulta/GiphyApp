package ru.kekulta.giphyapp.features.list.ui

import android.util.Log
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
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListViewHolder(
    private val binding: ListItemGifBinding, val onClickListener: AdapterClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.gifCard.setOnClickListener { onClickListener.onClick(adapterPosition) }

    }

    fun onBind(gif: Gif) {
        Log.d(LOG_TAG, gif.urlPreview.toString())
        val circularProgressDrawable = CircularProgressDrawable(binding.gifIv.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(binding.gifIv.context)
            .asGif()
            .load(gif.urlPreview)
            .override(gif.width, gif.height)
            .placeholder(circularProgressDrawable)
            // TODO add error drawable
            .error(R.drawable.ic_launcher_background)
            .into(binding.gifIv)
        binding.gifLikeButton.setOnCheckedChangeListener(null)
        binding.gifLikeButton.isChecked = gif.liked
        binding.gifLikeButton.setOnCheckedChangeListener { _, _ ->
            onClickListener.onLikeClick(adapterPosition)
        }
    }

    companion object {
        const val LOG_TAG = "GifListViewHolder"
    }
}