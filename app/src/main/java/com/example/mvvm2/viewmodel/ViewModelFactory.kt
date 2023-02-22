package com.example.mvvm2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.room.RecordRepository

class ViewModelFactory(private val repository: RecordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RecordRepository::class.java).newInstance(repository)
    }
}