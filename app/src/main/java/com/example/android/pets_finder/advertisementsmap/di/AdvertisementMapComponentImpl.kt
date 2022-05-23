package com.example.android.pets_finder.advertisementsmap.di

import com.example.android.pets_finder.advertisementsmap.AdvertisementsMapFragment
import dagger.Subcomponent

@AdvertisementMapScope
@Subcomponent(
    modules = [
        AdvertisementMapViewModelModule::class
    ]
)
interface AdvertisementMapComponentImpl : AdvertisementMapComponent {
    override fun inject(advertisementsMapFragment: AdvertisementsMapFragment)
}
