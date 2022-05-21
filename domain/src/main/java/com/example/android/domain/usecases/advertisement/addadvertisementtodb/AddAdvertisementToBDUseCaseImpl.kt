package com.example.android.domain.usecases.advertisement.addadvertisementtodb

import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.repositories.AdvertisementRepository

class AddAdvertisementToBDUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : AddAdvertisementToBDUseCase {
    override suspend fun execute(advertisement: AdvertisementModel): CreateAdvertisementUiState {
        return advertisementRepository.addAdvertisementToBD(advertisement)
    }
}
