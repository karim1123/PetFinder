package com.example.android.pets_finder.advertisementdetails.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.advertisementdetails.AdvertisementDetailsViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdvertisementDetailsViewModelModule {
    @AdvertisementDetailScope
    @Binds
    @IntoMap
    @ViewModelKey(AdvertisementDetailsViewModel::class)
    abstract fun bindAdvertisementDetailsViewModel(viewModel: AdvertisementDetailsViewModel): ViewModel
}
