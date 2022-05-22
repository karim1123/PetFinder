package com.example.android.domain.usecases.advertisement.addimagestostorage

import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.repositories.AdvertisementRepository

class AddAdvertisementImagesToStorageUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : AddAdvertisementImagesToStorageUseCase {
    override suspend fun execute(
        advertisement: AdvertisementModel
    ): CreateAdvertisementUiState {
        return advertisementRepository.addAdvertisementImagesToStorage(advertisement)
    }
}
