package ru.kekulta.giphyapp.features.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.giphyapp.databinding.ListItemGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifListAdapter(val fragment: Fragment) : RecyclerView.Adapter<GifListViewHolder>() {
    var gifList: List<Gif> = listOf()
        set(value) {
            field = value
            //TODO Implement DiffUtil
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifListViewHolder {
        return GifListViewHolder(
            fragment,
            gifList,
            ListItemGifBinding.inflate(LayoutInflater.from(parent.context)),
        )
    }

    override fun getItemCount(): Int = gifList.size


    override fun onBindViewHolder(holder: GifListViewHolder, position: Int) {
        holder.onBind(gifList[position])
    }
}