package com.example.android.domain.usecases.advertisement.getimagesuris

import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel

interface GetImagesUrisUseCase {
    suspend fun execute(advertisement: AdvertisementModel, size: Int): CreateAdvertisementUiState
}
