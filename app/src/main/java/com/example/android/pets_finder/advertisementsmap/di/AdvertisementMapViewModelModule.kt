package com.example.android.pets_finder.advertisementsmap.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.advertisementsmap.AdvertisementMapViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdvertisementMapViewModelModule {
    @AdvertisementMapScope
    @Binds
    @IntoMap
    @ViewModelKey(AdvertisementMapViewModel::class)
    abstract fun bindAdvertisementMapViewMode(viewModel: AdvertisementMapViewModel): ViewModel
}
