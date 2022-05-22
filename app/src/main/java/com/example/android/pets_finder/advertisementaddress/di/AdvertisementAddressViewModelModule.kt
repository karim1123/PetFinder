package com.example.android.pets_finder.advertisementaddress.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.advertisementaddress.AdvertisementAddressViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdvertisementAddressViewModelModule {
    @AdvertisementAddressScope
    @Binds
    @IntoMap
    @ViewModelKey(AdvertisementAddressViewModel::class)
    abstract fun bindAdvertisementAddressViewMode(viewModel: AdvertisementAddressViewModel): ViewModel
}
