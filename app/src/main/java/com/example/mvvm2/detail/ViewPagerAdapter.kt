package com.example.mvvm2.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm2.databinding.ActivityImageViewBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerHolder>() {

    var uriList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        return ViewPagerHolder(ActivityImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        var uri = uriList[position]
        holder.bind(uri)
    }

    override fun getItemCount(): Int {
        return uriList.size
    }
}

class ViewPagerHolder(private val binding: ActivityImageViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imgUri: String) {
        Glide.with(itemView)
            .load(imgUri)
            .into(binding.recordImage)
    }
}