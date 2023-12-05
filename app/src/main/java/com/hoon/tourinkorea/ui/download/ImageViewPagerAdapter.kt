package com.hoon.tourinkorea.ui.download

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hoon.tourinkorea.databinding.ItemDownloadImageBinding

class ImageViewPagerAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDownloadImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    class ViewHolder(private val binding: ItemDownloadImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String?) {
            binding.ivDownload.load(imageUrl) {
                crossfade(true)
                listener { _, _ ->
                    Log.e("ImageAdapter", "Image loaded: $imageUrl")

                }
            }
        }
    }
}
