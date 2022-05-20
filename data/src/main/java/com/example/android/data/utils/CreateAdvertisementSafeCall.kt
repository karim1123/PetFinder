package com.example.android.data.utils

import com.example.android.domain.common.CreateAdvertisementUiState

inline fun createAdvertisementSafeCall(action: () -> CreateAdvertisementUiState): CreateAdvertisementUiState {
    return try {
        action()
    } catch (e: Exception) {
        CreateAdvertisementUiState.Error(e.message ?: "An unknown Error Occurred")
    }
}
