package com.example.android.pets_finder.application

import android.app.Application

class PetFinderApplication : Application(), ApplicationContainer {
    private val appComponentImpl: AppComponentImpl by lazy {
        DaggerAppComponentImpl.builder().appModule(AppModule(this)).build()
    }

    override fun getAppComponent(): AppComponent {
        return appComponentImpl
    }
}
