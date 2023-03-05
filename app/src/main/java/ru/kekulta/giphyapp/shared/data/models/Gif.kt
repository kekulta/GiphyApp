package ru.kekulta.giphyapp.shared.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize


@Parcelize
data class Gif(
    val id: String,
    val width: Int,
    val height: Int,
    val urlPreview: String?,
    val urlOriginal: String?,
    val urlDownsized: String?,
    val user: User?,
    val title: String?,
    val contentDescription: String?,
    val liked: Boolean = false
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gif>() {
            override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean =
                oldItem.id == newItem.id && oldItem.liked == newItem.liked

        }
    }
}