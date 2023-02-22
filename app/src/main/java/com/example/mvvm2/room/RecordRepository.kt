package com.example.mvvm2.room

import com.example.mvvm2.entity.RecordEntity

class RecordRepository {

    private val appDBInstance = GlobalApplication.recordDatabaseInstance.recordDAO()

    suspend fun saveData(record: RecordEntity) = appDBInstance.saveData(record)
}