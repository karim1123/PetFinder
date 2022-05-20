package com.example.android.pets_finder.registration.di

import com.example.android.pets_finder.registration.RegistrationFragment
import dagger.Subcomponent

@RegistrationScope
@Subcomponent(
    modules = [
        RegistrationViewModelModule::class,
        RegistrationModule::class
    ]
)
interface RegistrationComponentImpl : RegistrationComponent {
    override fun inject(registrationFragment: RegistrationFragment)
}
