package com.example.android.pets_finder.advertisementaddress

import androidx.lifecycle.ViewModel
import com.example.android.pets_finder.utils.ParcelizeAdvertisementModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AdvertisementAddressViewModel @Inject constructor() : ViewModel() {
    val advertisement = MutableStateFlow(ParcelizeAdvertisementModel())
}
