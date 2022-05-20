package com.example.android.domain.common

import com.example.android.domain.entities.AdvertisementModel

sealed class LatestAdvertisementListUiState {
    data class Success(
        val advertisements: List<AdvertisementModel>
    ) : LatestAdvertisementListUiState()

    data class Error(
        val exception: String
    ) : LatestAdvertisementListUiState()

    object Loading : LatestAdvertisementListUiState()
}
