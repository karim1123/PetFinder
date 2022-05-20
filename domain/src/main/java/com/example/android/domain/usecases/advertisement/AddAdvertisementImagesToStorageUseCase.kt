package com.example.android.domain.usecases.advertisement

import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.common.CreateAdvertisementUiState

interface AddAdvertisementImagesToStorageUseCase {
    suspend fun execute(
        advertisement: AdvertisementModel,
        imagesUris: MutableList<String>
    ): CreateAdvertisementUiState
}
