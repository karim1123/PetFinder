package com.example.android.domain.usecases.advertisement.deleteadvertisement

import com.example.android.domain.common.AdvertisementDetailsUiState
import com.example.android.domain.repositories.AdvertisementRepository

class DeleteAdvertisementUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : DeleteAdvertisementUseCase {
    override suspend fun execute(advertisementId: String): AdvertisementDetailsUiState {
        return advertisementRepository.deleteAdvertisement(advertisementId)
    }
}
