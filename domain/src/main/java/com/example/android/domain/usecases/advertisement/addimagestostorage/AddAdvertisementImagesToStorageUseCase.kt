package com.example.android.domain.usecases.advertisement.addimagestostorage

import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel

interface AddAdvertisementImagesToStorageUseCase {
    suspend fun execute(
        advertisement: AdvertisementModel
    ): CreateAdvertisementUiState
}
