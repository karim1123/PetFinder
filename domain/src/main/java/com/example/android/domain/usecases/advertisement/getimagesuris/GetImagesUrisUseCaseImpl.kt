package com.example.android.domain.usecases.advertisement.getimagesuris

import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.repositories.AdvertisementRepository

class GetImagesUrisUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : GetImagesUrisUseCase {
    override suspend fun execute(
        advertisement: AdvertisementModel
    ): CreateAdvertisementUiState {
        return advertisementRepository.getImagesUris(advertisement)
    }
}
