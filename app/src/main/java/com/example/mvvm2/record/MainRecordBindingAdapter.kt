package com.example.mvvm2.record

import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvm2.MainActivity
import com.example.mvvm2.databinding.FragmentMainRecordBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.viewmodel.RecordViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainRecordBindingAdapter {
}


/** viewModel */
lateinit var recordViewModel: RecordViewModel

/** uri 담을 MutableList*/
private var uriList: MutableList<String> = mutableListOf()

@BindingAdapter("imgUri")
fun loadImage(imageView: ImageView, imageList: String) {

    var uriList = imageList.split("^")

    Glide.with(imageView.context)
        .load(uriList[0])
        .into(imageView)
}


