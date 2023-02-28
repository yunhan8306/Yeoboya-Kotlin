package com.example.mvvm2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository


class MainViewModel(private val repository: RecordRepository) : ViewModel() {

    /** 이전 프래그먼트 */
    var agoFragment = ""

    /** detail_frag visibility */
    val visibilityDetailFragment = MutableLiveData<Boolean>()

    /** selectRecord */
    lateinit var selectRecord: RecordEntity

    /**
     * detail fragment visibility set
     * false - gone
     * true - visible
     * */
    fun setVisibilityDetailFragment(TF: Boolean) {
        visibilityDetailFragment.value = TF
    }
}