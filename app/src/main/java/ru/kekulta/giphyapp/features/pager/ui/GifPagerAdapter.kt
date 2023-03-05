package ru.kekulta.giphyapp.features.pager.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(
        fragment
    ) {

    var items: List<Gif> = emptyList()
        set(value) {

            // TODO DiffUtil
            val diffIndex = diff(items, value)
            Log.d(LOG_TAG, "dffIndex: $diffIndex")
            field = value
            if (diffIndex != null) {
                notifyItemRangeChanged(diffIndex, items.size)
                Log.d(LOG_TAG, "Item $diffIndex removed!")
            }
            notifyDataSetChanged()
            Log.d(LOG_TAG, "Data Set changed!")

        }

    private fun diff(oldList: List<Gif>, newList: List<Gif>): Int? {
        Log.d(LOG_TAG, "oldList: ${oldList.map { it.id }}\n newList: ${newList.map { it.id }}")
        val oldSet = oldList.map { it.id }.toSet()
        val newSet = newList.map { it.id }.toSet()
        val diffSet = oldSet - newSet
        if (oldList.size - 1 == newList.size && diffSet.size == 1) {
            return oldList.indexOfFirst { it.id == diffSet.first() }
        }
        return null
    }


    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(LOG_TAG, "created fragment for position $position, id: ${items[position].id}")
        return GifFragment.newInstance(items[position])
    }

    companion object {
        const val LOG_TAG = "GifPagerAdapter"
    }

}