package com.example.android.pets_finder.createadvertisement

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.pets_finder.databinding.ItemCreateAdvertisementImagesListBinding

class CreateAdvertisementImagesListItemAdapter :
    RecyclerView.Adapter<CreateAdvertisementImagesListItemAdapter.ImageViewHolder>() {
    private var selectedImagesUris = mutableListOf<String>()
    private var isCreateAdvertisementFragment = true

    inner class ImageViewHolder(
        binding: ItemCreateAdvertisementImagesListBinding,
        val context: android.content.Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.selectedImage
        private val cancelButton = binding.cancelBtn

        // проверка в каком фрагменте используется адаптер,
        // если CreateAdvertisementFragment, то отображаем кнопку удаления картинки,
        // если AdvertisementDetailsFragment, то не отображаем
        init {
            if (!isCreateAdvertisementFragment) {
                cancelButton.isVisible = false
            }
        }

        fun bind(property: String, index: Int) {
            val uri = Uri.parse(property)
            Glide.with(context).load(uri).centerCrop().into(image)
            // удаление картинки по нажатию кнопки удаления
            cancelButton.setOnClickListener {
                deleteItem(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCreateAdvertisementImagesListBinding.inflate(
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
        holder.bind(selectedImagesUris[position], position)
    }

    fun deleteItem(index: Int) {
        selectedImagesUris.removeAt(index)
        val itemCount = selectedImagesUris.size - index
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    fun addSelectedImagesUris(items: List<String>, fragmentFlag: Boolean) {
        isCreateAdvertisementFragment = fragmentFlag
        selectedImagesUris = items.toMutableList()
        val startPosition = selectedImagesUris.size
        notifyItemRangeChanged(startPosition, items.size)
    }
}
