package com.hoon.tourinkorea.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hoon.tourinkorea.ItemClickListener
import com.hoon.tourinkorea.data.model.BookmarkEntity
import com.hoon.tourinkorea.databinding.ItemBookmarkListBinding

class BookmarkAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<BookmarkEntity, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding =
            ItemBookmarkListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.bind(bookmark)
    }

    override fun submitList(bookmarks: List<BookmarkEntity>?) {
        super.submitList(bookmarks)
        notifyDataSetChanged()
    }

    inner class BookmarkViewHolder(private val binding: ItemBookmarkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: BookmarkEntity) {
            binding.post = bookmark.post
            binding.ivBookmarkPhoto.load(bookmark.post.storageUriList.firstOrNull())

            itemView.setOnClickListener {
                itemClickListener.onItemClick(bookmark.post, itemView)
            }
        }
    }

    private class BookmarkDiffCallback : DiffUtil.ItemCallback<BookmarkEntity>() {
        override fun areItemsTheSame(oldItem: BookmarkEntity, newItem: BookmarkEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookmarkEntity, newItem: BookmarkEntity): Boolean {
            return oldItem == newItem
        }
    }
}