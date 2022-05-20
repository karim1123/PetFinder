package com.example.android.pets_finder.advertisementlist.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.advertisementlist.AdvertisementListViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.multibindings.IntoMap
import dagger.Module

@Module
abstract class AdvertisementListViewModelModule {
    @AdvertisementListScope
    @Binds
    @IntoMap
    @ViewModelKey(AdvertisementListViewModel::class)
    abstract fun bindAdvertisementListViewModel(viewModel: AdvertisementListViewModel): ViewModel
}
