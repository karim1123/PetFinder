package com.example.android.domain.repositories

import com.example.android.domain.common.AdvertisementDetailsUiState
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel

interface AdvertisementRepository {
    suspend fun addAdvertisementToBD(
        advertisementModel: AdvertisementModel
    ): CreateAdvertisementUiState

    suspend fun addAdvertisementImagesToStorage(
        advertisementModel: AdvertisementModel,
        imagesUris: MutableList<String>
    ): CreateAdvertisementUiState

    suspend fun getImagesUris(
        advertisement: AdvertisementModel,
        size: Int
    ): CreateAdvertisementUiState

    suspend fun deleteAdvertisement(advertisementId: String): AdvertisementDetailsUiState
}
