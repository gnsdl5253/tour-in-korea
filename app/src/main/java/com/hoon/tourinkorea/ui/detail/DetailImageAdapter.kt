package com.hoon.tourinkorea.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hoon.tourinkorea.databinding.ItemDetailImageBinding

class DetailImageAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<DetailImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(private val binding: ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            binding.ivImages.load(imageUrl) {
                crossfade(true)
                listener { _, _ ->
                    Log.e("ImageAdapter", "Image loaded: $imageUrl")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size
}