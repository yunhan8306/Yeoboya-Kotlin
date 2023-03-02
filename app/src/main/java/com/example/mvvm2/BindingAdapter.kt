package com.example.mvvm2

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvm2.MainActivity.Companion.TAG

@BindingAdapter("imgUri")
fun ImageView.loadImage(imageList: String) {
    imageList.takeIf { it.isNotEmpty() }?.split("^")?.get(0)?.also { path ->
        Log.d(TAG, "path - $path")
        Glide.with(context)
            .load(path)
            .into(this)
    }
}


