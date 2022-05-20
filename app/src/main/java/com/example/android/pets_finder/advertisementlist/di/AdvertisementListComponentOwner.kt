package com.example.android.pets_finder.advertisementlist.di

import com.example.android.pets_finder.advertisementlist.AdvertisementListFragment

interface AdvertisementListComponentOwner {
    fun inject(advertisementListFragment: AdvertisementListFragment)
}
