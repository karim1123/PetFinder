package com.example.android.domain.entities

data class AdvertisementModel(
    var advertisementId: String = "",
    var userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhoneNumber: String = "",
    var petStatus: String = "",
    var petType: String = "",
    var address: String = "",
    val urisList: MutableList<String> = mutableListOf()
)
