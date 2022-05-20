package com.example.android.domain.common

import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.entities.UserModel

sealed class CreateAdvertisementUiState {
    data class Success(
        val user: UserModel? = null,
        val advertisement: AdvertisementModel? = null,
        var closeFlag: Boolean = false
    ) : CreateAdvertisementUiState()
    data class Error(val exception: String) : CreateAdvertisementUiState()
    object Loading : CreateAdvertisementUiState()
}
