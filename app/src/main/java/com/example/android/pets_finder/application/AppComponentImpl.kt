package com.example.android.pets_finder.application

import com.example.android.pets_finder.advertisementaddress.di.AdvertisementAddressComponentImpl
import com.example.android.pets_finder.advertisementdetails.di.AdvertisementDetailsComponentImpl
import com.example.android.pets_finder.advertisementlist.di.AdvertisementListComponentImpl
import com.example.android.pets_finder.advertisementsmap.di.AdvertisementMapComponentImpl
import com.example.android.pets_finder.createadvertisement.di.CreateAdvertisementComponentImpl
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
interface AppComponentImpl : AppComponent {
    override fun plusRegistrationComponent(): RegistrationComponentImpl
    override fun plusLoginComponent(): LoginComponentImpl
    override fun plusCreateAdvertisementComponent(): CreateAdvertisementComponentImpl
    override fun plusAdvertisementListComponent(): AdvertisementListComponentImpl
    override fun plusAdvertisementDetailsComponent(): AdvertisementDetailsComponentImpl
    override fun plusAdvertisementAddressComponent(): AdvertisementAddressComponentImpl
    override fun plusAdvertisementMapComponent(): AdvertisementMapComponentImpl
}
