package com.example.android.pets_finder.application

import com.example.android.pets_finder.advertisementlist.di.AdvertisementListComponent
import com.example.android.pets_finder.createadvertisement.di.CreateAdvertisementComponent
import com.example.android.pets_finder.login.di.LoginComponentImpl
import com.example.android.pets_finder.registration.di.RegistrationComponentImpl
import com.example.android.pets_finder.viewModelFactory.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AppComponentOwner {
    override fun plusRegistrationComponent(): RegistrationComponentImpl
    override fun plusLoginComponent(): LoginComponentImpl
    override fun plusCreateAdvertisementComponent(): CreateAdvertisementComponent
    override fun plusAdvertisementListComponent(): AdvertisementListComponent
}
