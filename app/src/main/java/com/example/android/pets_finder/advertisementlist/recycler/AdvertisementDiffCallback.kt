package com.example.android.pets_finder.advertisementlist.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.android.domain.entities.AdvertisementModel

object AdvertisementDiffCallback : DiffUtil.ItemCallback<AdvertisementModel>() {
    override fun areItemsTheSame(
        oldItem: AdvertisementModel,
        newItem: AdvertisementModel
    ): Boolean {
        return (oldItem.advertisementId == newItem.advertisementId)
    }

    override fun areContentsTheSame(
        oldItem: AdvertisementModel,
        newItem: AdvertisementModel
    ): Boolean {
        return oldItem == newItem
    }
}
