package com.hoon.tourinkorea.ui.write

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.tourinkorea.databinding.ItemSelectPhotoBinding

class PostSelectAdapter(private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<PostSelectAdapter.SelectedImageViewHolder>() {

    private var itemList = listOf<Uri>()

    class SelectedImageViewHolder(
        private val binding: ItemSelectPhotoBinding,
        private val clickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri, position: Int) {
            binding.ivSelectedImage.setImageURI(uri)
            binding.btnDeleteImage.setOnClickListener {
                clickListener.invoke(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        val binding =
            ItemSelectPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedImageViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        holder.bind(itemList[position],position)
    }

    fun updateData(uriList: List<Uri>) {
        val startPosition = itemList.size
        itemList = uriList
        notifyItemRangeInserted(startPosition, uriList.size)
    }
}