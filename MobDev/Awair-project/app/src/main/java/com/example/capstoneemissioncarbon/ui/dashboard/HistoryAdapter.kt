package com.example.capstoneemissioncarbon.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneemissioncarbon.R
import com.example.capstoneemissioncarbon.databinding.ItemHistoryBinding
import com.example.capstoneemissioncarbon.ui.profile.CurrentConditions

class HistoryAdapter : ListAdapter<CurrentConditions, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CurrentConditions) {
            binding.apply {
                // Bind data to views
                tvAlertTitle.text = data.title
                tvDescription.text = data.description
                tvStatus.text = data.status

                // Change status background color
                tvStatus.setBackgroundResource(
                    if (data.status == "Unhealthy") R.drawable.status_red
                    else R.drawable.status_green
                )
            }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<CurrentConditions>() {
    override fun areItemsTheSame(oldItem: CurrentConditions, newItem: CurrentConditions): Boolean {
        // Ganti logika identifikasi unik jika tidak menggunakan date
        return oldItem == newItem // Gunakan id sebagai identifier unik
    }

    override fun areContentsTheSame(oldItem: CurrentConditions, newItem: CurrentConditions): Boolean {
        return oldItem == newItem // Bandingkan seluruh objek untuk memastikan kontennya sama
    }
}

