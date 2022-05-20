package com.example.android.pets_finder.createadvertisement.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.createadvertisement.CreateAdvertisementViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CreateAdvertisementViewModelModule {
    @CreateAdvertisementScope
    @Binds
    @IntoMap
    @ViewModelKey(CreateAdvertisementViewModel::class)
    abstract fun bindCreateAdvertisementViewModel(viewModel: CreateAdvertisementViewModel): ViewModel
}
