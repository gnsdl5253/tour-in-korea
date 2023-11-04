package com.hoon.tourinkorea.ui.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.tourinkorea.R
import com.hoon.tourinkorea.databinding.ItemPhotoCountBinding

class ImageCountAdapter(private val clickListener: () -> Unit) :
    RecyclerView.Adapter<ImageCountAdapter.ImageCountViewHolder>() {

    private var selectImageCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCountViewHolder {
        val binding =
            ItemPhotoCountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageCountViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ImageCountViewHolder, position: Int) {
        holder.bind(selectImageCount)
    }

    fun updateData(count: Int) {
        selectImageCount = count
        notifyItemChanged(0)
    }

    class ImageCountViewHolder(
        private val binding: ItemPhotoCountBinding,
        private val clickListener: () -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(count: Int) {
            itemView.setOnClickListener {
                clickListener.invoke()
            }
            binding.tvImageCount.text =
                itemView.resources.getString(R.string.img_count, count)

        }
    }
}
