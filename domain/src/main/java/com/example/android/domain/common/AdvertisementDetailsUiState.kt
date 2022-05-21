package com.example.android.domain.common

import com.example.android.domain.entities.UserModel

sealed class AdvertisementDetailsUiState {
    data class Success(
        val user: UserModel? = null,
        val deleteFlag: Boolean = false
    ) : AdvertisementDetailsUiState()

    data class Error(val exception: String) : AdvertisementDetailsUiState()
    object Loading : AdvertisementDetailsUiState()
}
