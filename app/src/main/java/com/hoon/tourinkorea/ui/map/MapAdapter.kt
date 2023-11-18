package com.hoon.tourinkorea.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.tourinkorea.databinding.ItemMapListBinding

class MapAdapter (private val itemList: ArrayList<Place>) : RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMapListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMapListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = itemList[position]
        val binding = holder.binding
        binding.tvListName.text = place.place_name
        binding.tvListRoad.text = place.road_address_name
        binding.tvListAddress.text = place.address_name

        binding.root.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}