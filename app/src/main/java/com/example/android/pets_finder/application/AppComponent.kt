package com.example.android.pets_finder.application

import com.example.android.pets_finder.advertisementaddress.di.AdvertisementAddressComponent
import com.example.android.pets_finder.advertisementdetails.di.AdvertisementDetailsComponent
import com.example.android.pets_finder.advertisementlist.di.AdvertisementListComponent
import com.example.android.pets_finder.advertisementsmap.di.AdvertisementMapComponent
import com.example.android.pets_finder.createadvertisement.di.CreateAdvertisementComponent
import com.example.android.pets_finder.login.di.LoginComponent
import com.example.android.pets_finder.registration.di.RegistrationComponent

interface AppComponent {
    fun plusRegistrationComponent(): RegistrationComponent
    fun plusLoginComponent(): LoginComponent
    fun plusCreateAdvertisementComponent(): CreateAdvertisementComponent
    fun plusAdvertisementListComponent(): AdvertisementListComponent
    fun plusAdvertisementDetailsComponent(): AdvertisementDetailsComponent
    fun plusAdvertisementAddressComponent(): AdvertisementAddressComponent
    fun plusAdvertisementMapComponent(): AdvertisementMapComponent
}
