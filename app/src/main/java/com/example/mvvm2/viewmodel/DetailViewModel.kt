package com.example.mvvm2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecordRepository): ViewModel() {

    val isGetNoDataComplete = MutableLiveData<RecordEntity>()

    /** no에 대한 data 조회 */
    fun getNoData(no: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getNoData(no).let {
                isGetNoDataComplete.postValue(it)
            }
        }
    }

}