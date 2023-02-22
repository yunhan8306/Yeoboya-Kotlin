package com.example.mvvm2.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayViewModel(private val repository: RecordRepository): ViewModel() {

    val isGetDateDataComplete = MutableLiveData<List<RecordEntity>>()

    /** today data 조회 */
    fun getDateData(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getDateData(date).let {
                isGetDateDataComplete.postValue(it)
            }
        }
    }

    /** detail activity 인텐트 */
//    fun viewDetail(no: Long){
//        val intent = Intent()
//        intent.putExtra("no", no)
//        ContextCompat.startActivity()
//    }

}