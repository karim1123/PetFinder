package com.example.android.domain.repositories

import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.common.CreateAdvertisementUiState

interface AdvertisementRepository {
    suspend fun addAdvertisementToBD(
        advertisementModel: AdvertisementModel
    ): CreateAdvertisementUiState

    suspend fun addAdvertisementImagesToStorage(
        advertisementModel: AdvertisementModel,
        imagesUris: MutableList<String>
    ): CreateAdvertisementUiState
}
