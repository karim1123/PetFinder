package com.example.android.pets_finder.utils

import com.example.android.domain.entities.AdvertisementModel

object MapperUtils {
    fun AdvertisementModel.mapToParcelize(): ParcelizeAdvertisementModel {
        return ParcelizeAdvertisementModel(
            advertisementId,
            userId,
            petStatus,
            petType,
            address,
            description,
            urisList,
            latitude,
            longitude
        )
    }

    fun ParcelizeAdvertisementModel.mapToAdvertisementModel(): AdvertisementModel {
        return AdvertisementModel(
            advertisementId,
            userId,
            petStatus,
            petType,
            address,
            description,
            urisList,
            latitude,
            longitude
        )
    }
}
