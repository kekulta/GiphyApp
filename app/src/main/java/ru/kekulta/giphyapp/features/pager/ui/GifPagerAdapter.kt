package ru.kekulta.giphyapp.features.pager.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifPagerAdapter(fragment: Fragment) :
// TODO viewpager2
    FragmentStatePagerAdapter(
        fragment.childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    var items: List<Gif> = emptyList()
        set(value) {
            field = value
            // TODO DiffUtil
            notifyDataSetChanged()
            Log.d(LOG_TAG, "Data Set changed!")
        }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Fragment {
        return GifFragment.newInstance(items[position])
    }

    companion object {
        const val LOG_TAG = "GifPagerAdapter"
    }
}