package com.example.android.pets_finder.advertisementlist.di

import com.example.android.pets_finder.advertisementlist.AdvertisementListFragment
import dagger.Subcomponent

@AdvertisementListScope
@Subcomponent(
    modules = [
        AdvertisementListViewModelModule::class
    ]
)
interface AdvertisementListComponentImpl : AdvertisementListComponent {
    override fun inject(advertisementListFragment: AdvertisementListFragment)
}
