package ru.kekulta.giphyapp.features.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.kekulta.giphyapp.shared.data.models.Gif

class GifPagerAdapter(fragment: Fragment) :
// TODO ViewPager2 doesn't support shared transitions in the same way and has lack of documentation about that
    FragmentStatePagerAdapter(
        fragment.childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    var items: List<Gif> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Fragment {
        return GifFragment.newInstance(items[position])
    }
}