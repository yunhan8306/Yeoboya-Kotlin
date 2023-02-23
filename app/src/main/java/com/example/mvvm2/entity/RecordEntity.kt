package com.example.mvvm2.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "record_model")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    var no: Long = 0L,
    var title: String,
    var content: String,
    var date: String,
    var time: String,
    var uriList: String,
    var bookMark: Int = 0
) : Parcelable