package com.example.android.domain.entities

data class AdvertisementModel(
    var advertisementId: String = "",
    var userId: String = "",
    var petStatus: String = "",
    var petType: String = "",
    var address: String = "",
    var description: String = "",
    var urisList: MutableList<String> = mutableListOf(),
    var latitude: Double? = null,
    var longitude: Double? = null
)
