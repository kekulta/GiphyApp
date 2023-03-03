package ru.kekulta.giphyapp.features.search.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.giphyapp.databinding.ListItemGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListAdapter(val fragment: Fragment) : RecyclerView.Adapter<GifListViewHolder>(),
    AdapterClickListener {
    private var adapterClickListener: AdapterClickListener? = null
    var gifList: List<Gif> = listOf()
        set(value) {
            field = value
            //TODO Implement DiffUtil
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifListViewHolder {
        return GifListViewHolder(
            ListItemGifBinding.inflate(LayoutInflater.from(parent.context)), this,
        )
    }

    override fun getItemCount(): Int = gifList.size


    override fun onBindViewHolder(holder: GifListViewHolder, position: Int) {
        holder.onBind(gifList[position])
    }

    override fun onClick(adapterPosition: Int) {
        adapterClickListener?.onClick(adapterPosition)
    }

    fun setAdapterClickListener(adapterClickListener: AdapterClickListener?) {
        this.adapterClickListener = adapterClickListener
    }

    fun setAdapterClickListener(adapterClickListener: (Int) -> (Unit)) {
        this.adapterClickListener = object : AdapterClickListener {
            override fun onClick(adapterPosition: Int) {
                adapterClickListener(adapterPosition)
            }
        }
    }
}
