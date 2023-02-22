package com.example.mvvm2.room

import com.example.mvvm2.entity.RecordEntity

class RecordRepository {

    private val appDbInstance = GlobalApplication.recordDatabaseInstance.recordDAO()

    suspend fun saveData(record: RecordEntity) = appDbInstance.saveData(record)

    suspend fun getDateData(date: String) = appDbInstance.getDateData(date)

    suspend fun getNoData(no: Long) = appDbInstance.getNoData(no)


}