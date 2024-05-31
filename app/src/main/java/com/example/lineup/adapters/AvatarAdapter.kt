package com.example.lineup.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.lineup2024.databinding.CharacterLayoutBinding

class AvatarAdapter(private val images: IntArray) :
    RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageId = images[position]
        holder.binding.avatarImg.setImageResource(imageId)
    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(val binding: CharacterLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
