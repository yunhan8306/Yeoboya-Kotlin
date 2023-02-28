package com.example.mvvm2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(private val repository: RecordRepository): ViewModel() {

    val isSaveComplete = MutableLiveData<Long>()

    /** record 저장 */
    fun saveRecord(recordEntity: RecordEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveData(recordEntity).let {
                // 수정 필요
            isSaveComplete.postValue(Long.MAX_VALUE)
            }
        }
    }
}