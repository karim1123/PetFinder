package com.example.android.pets_finder.login.di

import com.example.android.pets_finder.login.LoginFragment
import dagger.Subcomponent

@LoginScope
@Subcomponent(
    modules = [
        LoginViewModelModule::class,
        LoginModule::class
    ]
)
interface LoginComponentImpl : LoginComponent {
    override fun inject(loginFragment: LoginFragment)
}
