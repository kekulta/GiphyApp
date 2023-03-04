package ru.kekulta.giphyapp.features.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.giphyapp.databinding.ListItemGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListAdapter() : ListAdapter<Gif, GifListViewHolder>(Gif.DIFF_CALLBACK),
    AdapterClickListener {
    private var adapterClickListener: AdapterClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifListViewHolder {
        return GifListViewHolder(
            ListItemGifBinding.inflate(LayoutInflater.from(parent.context)), this,
        )
    }

    override fun onBindViewHolder(holder: GifListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


    override fun onClick(adapterPosition: Int) {
        adapterClickListener?.onClick(adapterPosition)
    }

    override fun onLikeClick(adapterPosition: Int) {
        adapterClickListener?.onLikeClick(adapterPosition)
    }

    fun setAdapterClickListener(adapterClickListener: AdapterClickListener?) {
        this.adapterClickListener = adapterClickListener
    }
}
