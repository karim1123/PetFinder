package com.example.android.pets_finder.advertisementlist.di

import com.example.android.pets_finder.advertisementlist.AdvertisementListFragment
import dagger.Subcomponent

@AdvertisementListScope
@Subcomponent(
    modules = [
        AdvertisementListViewModelModule::class
    ]
)
interface AdvertisementListComponent : AdvertisementListComponentOwner {
    override fun inject(advertisementListFragment: AdvertisementListFragment)
}
