package com.example.mvvm2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecordRepository): ViewModel() {

    val isUpdateDataComplete = MutableLiveData<RecordEntity>()

    val isDeleteDataComplete = MutableLiveData<RecordEntity>()


    /** record 삭제 */
    fun deleteData(record: RecordEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteData(record).let {
                isDeleteDataComplete.postValue(record)
            }
        }
    }

    /** record 수정 */
    fun updateData(record: RecordEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateData(record).let {
                isUpdateDataComplete.postValue(record)
            }
        }
    }
}