package com.example.android.pets_finder.advertisementaddress.di

import com.example.android.pets_finder.advertisementaddress.AdvertisementAddressFragment
import dagger.Subcomponent

@AdvertisementAddressScope
@Subcomponent(
    modules = [
        AdvertisementAddressViewModelModule::class
    ]
)
interface AdvertisementAddressComponentImpl : AdvertisementAddressComponent {
    override fun inject(advertisementAddressFragment: AdvertisementAddressFragment)
}
