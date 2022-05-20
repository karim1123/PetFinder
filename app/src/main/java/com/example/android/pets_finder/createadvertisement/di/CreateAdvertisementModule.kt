package com.example.android.pets_finder.createadvertisement.di

import com.example.android.domain.repositories.AdvertisementRepository
import com.example.android.domain.usecases.advertisement.AddAdvertisementImagesToStorageUseCase
import com.example.android.domain.usecases.advertisement.AddAdvertisementImagesToStorageUseCaseImpl
import com.example.android.domain.usecases.advertisement.AddAdvertisementToBDUseCase
import com.example.android.domain.usecases.advertisement.AddAdvertisementToBDUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CreateAdvertisementModule {
    @CreateAdvertisementScope
    @Provides
    fun provideAddAdvertisementToBDUseCase(
        repository: AdvertisementRepository
    ): AddAdvertisementToBDUseCase = AddAdvertisementToBDUseCaseImpl(repository)

    @CreateAdvertisementScope
    @Provides
    fun provideAddAdvertisementImagesToStorageUseCase(
        repository: AdvertisementRepository
    ): AddAdvertisementImagesToStorageUseCase = AddAdvertisementImagesToStorageUseCaseImpl(repository)

    @CreateAdvertisementScope
    @Provides
    fun provideCreateAdvertisementDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
