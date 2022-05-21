package com.example.android.data.utils

import com.example.android.domain.common.AdvertisementDetailsUiState

inline fun advertisementDetailsSafeCall(action: () -> AdvertisementDetailsUiState): AdvertisementDetailsUiState {
    return try {
        action()
    } catch (e: Exception) {
        AdvertisementDetailsUiState.Error(e.message ?: "An unknown Error Occurred")
    }
}
