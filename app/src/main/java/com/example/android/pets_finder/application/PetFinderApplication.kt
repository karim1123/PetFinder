package com.example.android.pets_finder.application

import android.app.Application

class PetFinderApplication : Application(), ApplicationContainer {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun getAppComponent(): AppComponentOwner {
        return appComponent
    }
}
