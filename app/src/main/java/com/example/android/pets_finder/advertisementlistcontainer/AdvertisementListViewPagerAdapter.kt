package com.example.android.pets_finder.advertisementlistcontainer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.pets_finder.advertisementlist.AdvertisementListFragment

class AdvertisementListViewPagerAdapter(
    advertisementListContainerFragment: AdvertisementListContainerFragment,
    private val itemsCount: Int
): FragmentStateAdapter(advertisementListContainerFragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return AdvertisementListFragment.getInstance(position)
    }
}
