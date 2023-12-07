package com.hoon.tourinkorea.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hoon.tourinkorea.databinding.ItemMapListBinding

class MapAdapter(
    private val itemClickListener: MapItemClickListener,
) : ListAdapter<Place, MapAdapter.ViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMapListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place, itemClickListener)
    }

    class ViewHolder(val binding: ItemMapListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place, itemClickListener: MapItemClickListener) {
            binding.tvListName.text = place.place_name
            binding.tvListRoad.text = place.road_address_name
            binding.tvListAddress.text = place.address_name

            itemView.setOnClickListener {
                itemClickListener.onClick(place)
            }
        }
    }

    private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.address_name == newItem.address_name
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}