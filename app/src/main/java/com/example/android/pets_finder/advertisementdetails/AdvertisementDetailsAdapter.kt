package com.example.android.pets_finder.advertisementdetails

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.pets_finder.databinding.ItemDetailsAdvertisementImagesListBinding

class AdvertisementDetailsAdapter :
    RecyclerView.Adapter<AdvertisementDetailsAdapter.ImageViewHolder>() {
    private var selectedImagesUris = mutableListOf<String>()

    inner class ImageViewHolder(
        binding: ItemDetailsAdvertisementImagesListBinding,
        val context: android.content.Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.selectedImage

        fun bind(property: String) {
            val uri = Uri.parse(property)
            Glide.with(context).load(uri).centerCrop().into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailsAdvertisementImagesListBinding.inflate(
            inflater,
            parent,
            false
        )
        return ImageViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return selectedImagesUris.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(selectedImagesUris[position])
    }

    fun addSelectedImagesUris(items: List<String>) {
        selectedImagesUris = items.toMutableList()
    }
}
