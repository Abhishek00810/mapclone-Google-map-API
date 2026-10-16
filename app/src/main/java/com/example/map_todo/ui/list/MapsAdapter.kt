package com.example.map_todo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map_todo.data.model.UserMap
import com.example.map_todo.databinding.ItemUserMapBinding

class MapsAdapter(
    private val userMaps: List<UserMap>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvMapTitle.text = userMaps[position].title
        holder.itemView.setOnClickListener { onItemClick(holder.bindingAdapterPosition) }
    }

    override fun getItemCount() = userMaps.size

    class ViewHolder(val binding: ItemUserMapBinding) : RecyclerView.ViewHolder(binding.root)
}
