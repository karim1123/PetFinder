package com.example.android.pets_finder.advertisementlist.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.pets_finder.R
import com.example.android.pets_finder.databinding.ItemAdvertisementListBinding

class AdvertisementListViewHolder(
    binding: ItemAdvertisementListBinding,
    val onClick: (AdvertisementModel) -> Unit,
    val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    private val advertisementTitle = binding.tvAdvertisementListItemTitle
    private val petAddress = binding.tvPetAddress
    private val petPhoto = binding.petPhoto
    private var currentAdvertisementModel: AdvertisementModel? = null

    init {
        itemView.setOnClickListener {
            currentAdvertisementModel?.let {
                onClick(it)
            }
        }
    }

    fun bind(advertisementModel: AdvertisementModel) {
        currentAdvertisementModel = advertisementModel
        advertisementTitle.text = context.getString(
            R.string.advertisement_list_item_title,
            advertisementModel.petStatus,
            advertisementModel.petType
        )
        petAddress.text = advertisementModel.address
        if (advertisementModel.urisList.isNotEmpty()) {
            Glide.with(context).load(advertisementModel.urisList.first()).circleCrop()
                .placeholder(R.drawable.ic_mage_placeholder).into(petPhoto)
        }
    }
}
