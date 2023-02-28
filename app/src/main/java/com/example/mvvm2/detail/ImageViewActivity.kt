package com.example.mvvm2.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        var uri = intent.getStringExtra("uri")

        Log.d(TAG, "ImageViewActivity - onCreate called")

    }


}