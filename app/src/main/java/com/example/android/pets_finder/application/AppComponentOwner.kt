package com.example.android.pets_finder.application

import com.example.android.pets_finder.advertisementlist.di.AdvertisementListComponentOwner
import com.example.android.pets_finder.createadvertisement.di.CreateAdvertisementComponentOwner
import com.example.android.pets_finder.login.di.LoginComponent
import com.example.android.pets_finder.registration.di.RegistrationComponent

interface AppComponentOwner {
    fun plusRegistrationComponent(): RegistrationComponent
    fun plusLoginComponent(): LoginComponent
    fun plusCreateAdvertisementComponent(): CreateAdvertisementComponentOwner
    fun plusAdvertisementListComponent(): AdvertisementListComponentOwner
}
