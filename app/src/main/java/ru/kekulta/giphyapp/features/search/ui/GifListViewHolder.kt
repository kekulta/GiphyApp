package ru.kekulta.giphyapp.features.search.ui

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ListItemGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListViewHolder(
    private val binding: ListItemGifBinding, private val onClickListener: AdapterClickListener
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

        val ta = binding.root.context.theme.obtainStyledAttributes(R.styleable.ThemeColors)
        val tint = ta.getColor(R.styleable.ThemeColors_colorPrimary, 1000)
        circularProgressDrawable.setColorSchemeColors(tint)

        circularProgressDrawable.start()

        Glide.with(binding.gifIv.context)
            .asGif()
            .load(gif.urlPreview)
            .override(gif.width, gif.height)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.error_outline)
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