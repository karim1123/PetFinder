package com.example.android.pets_finder.registration.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.registration.RegistrationViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RegistrationViewModelModule {
    @RegistrationScope
    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel
}
