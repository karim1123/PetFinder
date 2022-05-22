package com.example.android.pets_finder.createadvertisement

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.pets_finder.databinding.ItemCreateAdvertisementImagesListBinding

class CreateAdvertisementImagesListItemAdapter() :
    RecyclerView.Adapter<CreateAdvertisementImagesListItemAdapter.ImageViewHolder>() {
    lateinit var viewModel: CreateAdvertisementViewModel

    inner class ImageViewHolder(
        binding: ItemCreateAdvertisementImagesListBinding,
        val context: android.content.Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.selectedImage
        private val cancelButton = binding.cancelBtn

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
        return viewModel.advertisement.value.urisList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(viewModel.advertisement.value.urisList[position], position)
    }

    fun deleteItem(index: Int) {
        viewModel.advertisement.value.urisList.removeAt(index)
        val itemCount = viewModel.advertisement.value.urisList.size - index
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    fun addViewModel(createAdvertisementViewModel: CreateAdvertisementViewModel) {
        viewModel = createAdvertisementViewModel
    }
}
