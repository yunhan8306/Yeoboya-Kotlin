package com.example.mvvm2

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imgUri")
fun ImageView.loadImage(imageList: String) {
    imageList.split("^").takeIf { it.isNotEmpty() }?.map { it[0] }?.also { path ->
        Glide.with(context)
            .load(path)
            .into(this)
    }
}


