package com.example.mvvm2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TotalViewModel(private val repository: RecordRepository): ViewModel() {

    val isGetAllComplete = MutableLiveData<List<RecordEntity>>()

    /** 전체 data 조회 */
    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAll().let {
                isGetAllComplete.postValue(it)
            }
        }
    }
}