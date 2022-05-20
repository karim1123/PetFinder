package com.example.android.pets_finder.advertisementlist.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.pets_finder.databinding.ItemAdvertisementListBinding

class AdvertisementListItemAdapter(private val onClick: (AdvertisementModel) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<AdvertisementModel, AdvertisementListViewHolder>(
        AdvertisementDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAdvertisementListBinding.inflate(inflater, parent, false)
        return AdvertisementListViewHolder(binding, onClick, parent.context)
    }

    override fun onBindViewHolder(holder: AdvertisementListViewHolder, position: Int) {
        val advertisement = getItem(position)
        holder.bind(advertisement)
    }
}
