package com.example.android.domain.usecases.advertisement.deleteadvertisement

import com.example.android.domain.common.AdvertisementDetailsUiState

interface DeleteAdvertisementUseCase {
    suspend fun execute(advertisementId: String): AdvertisementDetailsUiState
}
