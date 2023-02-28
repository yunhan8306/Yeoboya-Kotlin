package com.example.mvvm2.grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm2.R
import com.example.mvvm2.databinding.ActivityImageViewBinding
import com.example.mvvm2.entity.RecordEntity

class MainGridRecyclerViewAdapter : RecyclerView.Adapter<MainGridRecyclerViewHolder>() {

    private val recordList = listOf<RecordEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainGridRecyclerViewHolder {
        return MainGridRecyclerViewHolder(ActivityImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainGridRecyclerViewHolder, position: Int) {
        val uri = recordList[position].uriList.split("^")[0]
        holder.bind(uri)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }
}

class MainGridRecyclerViewHolder(private val binding: ActivityImageViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: String){
        Glide.with(itemView)
            .load(uri)
            .error(R.drawable.blank_space)
            .into(binding.recordImage)
    }
}