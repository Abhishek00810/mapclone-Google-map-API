package com.example.map_todo

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.map_todo.model.user
import java.nio.file.Files.size
private const val TAG = "mapadapter"

class mapadapter(val context: Context, val users: List<user>, val onclickListener: OnclickListener): RecyclerView.Adapter<mapadapter.ViewHolder>(){

    interface OnclickListener{
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_map, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usermap = users[position]
        holder.itemView.setOnClickListener{
            Log.i(TAG, "On click $position")
            onclickListener.onItemClick(position)
        }
        val textviewtitle = holder.itemView.findViewById<TextView>(R.id.tvmaptitle)
        textviewtitle.text = usermap.title
    }

    override fun getItemCount() = users.size

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}