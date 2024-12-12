package com.example.capstoneemissioncarbon.ui.sign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneemissioncarbon.databinding.ItemSplashBinding

data class SplashItem(val imageRes: Int, val title: String, val description: String)

class SplashAdapter(private val items: List<SplashItem>) : RecyclerView.Adapter<SplashAdapter.SplashViewHolder>() {

    class SplashViewHolder(private val binding: ItemSplashBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SplashItem) {
            binding.imageView.setImageResource(item.imageRes)
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashViewHolder {
        val binding = ItemSplashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SplashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SplashViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
