package com.hoon.tourinkorea.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.databinding.ItemHomeListBinding

class HomeAdapter(private val onItemClick: (Post) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var postList: List<Post> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(ItemHomeListBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(postList[position], onItemClick)
    }

    fun updateList(updateList: List<Post>) {
        postList = updateList.reversed()
        notifyDataSetChanged()
    }

    class HomeViewHolder(
        private val  binding: ItemHomeListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, onItemClick: (Post) -> Unit) {
            Log.d("TAG", "Binding post: ${post.title}, URL: ${post.location}")
            binding.postList = post
            binding.root.setOnClickListener {
                onItemClick(post)
            }
        }
    }
}