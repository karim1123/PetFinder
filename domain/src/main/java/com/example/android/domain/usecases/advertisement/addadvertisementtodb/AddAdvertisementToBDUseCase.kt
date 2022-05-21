package com.example.android.domain.usecases.advertisement.addadvertisementtodb

import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.common.CreateAdvertisementUiState

interface AddAdvertisementToBDUseCase {
    suspend fun execute(advertisement: AdvertisementModel): CreateAdvertisementUiState
}
