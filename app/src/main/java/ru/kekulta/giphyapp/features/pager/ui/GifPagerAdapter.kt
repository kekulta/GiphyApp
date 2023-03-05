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
            field = value
            // TODO DiffUtil
            notifyDataSetChanged()
            Log.d(LOG_TAG, "Data Set changed!")
        }

    companion object {
        const val LOG_TAG = "GifPagerAdapter"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return GifFragment.newInstance(items[position])
    }
}