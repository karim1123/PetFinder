package com.example.android.pets_finder.login.di

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.login.LoginViewModel
import com.example.android.pets_finder.viewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelModule {
    @LoginScope
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}
