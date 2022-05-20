package com.example.android.pets_finder.createadvertisement.di

import com.example.android.pets_finder.createadvertisement.CreateAdvertisementFragment
import dagger.Subcomponent

@CreateAdvertisementScope
@Subcomponent(
    modules = [
        CreateAdvertisementViewModelModule::class,
        CreateAdvertisementModule::class
    ]
)
interface CreateAdvertisementComponent : CreateAdvertisementComponentOwner {
    override fun inject(createAdvertisementFragment: CreateAdvertisementFragment)
}
