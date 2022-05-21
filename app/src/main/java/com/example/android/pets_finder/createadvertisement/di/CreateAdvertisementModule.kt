package com.example.android.pets_finder.createadvertisement.di

import com.example.android.domain.repositories.AdvertisementRepository
import com.example.android.domain.usecases.advertisement.addimagestostorage.AddAdvertisementImagesToStorageUseCase
import com.example.android.domain.usecases.advertisement.addimagestostorage.AddAdvertisementImagesToStorageUseCaseImpl
import com.example.android.domain.usecases.advertisement.addadvertisementtodb.AddAdvertisementToBDUseCase
import com.example.android.domain.usecases.advertisement.addadvertisementtodb.AddAdvertisementToBDUseCaseImpl
import com.example.android.domain.usecases.advertisement.getimagesuris.GetImagesUrisUseCase
import com.example.android.domain.usecases.advertisement.getimagesuris.GetImagesUrisUseCaseImpl
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
    ): AddAdvertisementImagesToStorageUseCase =
        AddAdvertisementImagesToStorageUseCaseImpl(repository)

    @CreateAdvertisementScope
    @Provides
    fun provideGetImagesUrisStorageUseCase(
        repository: AdvertisementRepository
    ): GetImagesUrisUseCase = GetImagesUrisUseCaseImpl(repository)

    @CreateAdvertisementScope
    @Provides
    fun provideCreateAdvertisementDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
