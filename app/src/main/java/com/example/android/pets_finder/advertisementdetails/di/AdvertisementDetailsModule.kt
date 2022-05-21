package com.example.android.pets_finder.advertisementdetails.di

import com.example.android.domain.repositories.AdvertisementRepository
import com.example.android.domain.usecases.advertisement.deleteadvertisement.DeleteAdvertisementUseCase
import com.example.android.domain.usecases.advertisement.deleteadvertisement.DeleteAdvertisementUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class AdvertisementDetailsModule {
    @AdvertisementDetailScope
    @Provides
    fun provideAdvertisementDetailsDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AdvertisementDetailScope
    @Provides
    fun provideDeleteAdvertisementUseCase(
        repository: AdvertisementRepository
    ): DeleteAdvertisementUseCase = DeleteAdvertisementUseCaseImpl(repository)
}
