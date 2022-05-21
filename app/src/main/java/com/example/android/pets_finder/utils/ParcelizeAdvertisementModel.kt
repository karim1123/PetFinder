package com.example.android.pets_finder.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeAdvertisementModel(
    var advertisementId: String = "",
    var userId: String = "",
    var petStatus: String = "",
    var petType: String = "",
    var address: String = "",
    var description: String = "",
    val urisList: MutableList<String> = mutableListOf()
) : Parcelable
