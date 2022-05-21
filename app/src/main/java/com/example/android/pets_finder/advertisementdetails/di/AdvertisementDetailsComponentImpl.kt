package com.example.android.pets_finder.advertisementdetails.di

import com.example.android.pets_finder.advertisementdetails.AdvertisementDetailsFragment
import dagger.Subcomponent

@AdvertisementDetailScope
@Subcomponent(
    modules = [
        AdvertisementDetailsModule::class,
        AdvertisementDetailsViewModelModule::class
    ]
)
interface AdvertisementDetailsComponentImpl : AdvertisementDetailsComponent {
    override fun inject(advertisementDetailsFragment: AdvertisementDetailsFragment)
}
