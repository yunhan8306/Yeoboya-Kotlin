package com.example.mvvm2.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvm2.entity.RecordEntity


@Dao
interface RecordDAO {
    // 전체 data 가져오기
    @Query("SELECT * FROM record_model")
    suspend fun getAll(): List<RecordEntity>

    // date에 대한 data 가져오기
    @Query("SELECT * FROM record_model WHERE `date` = :date")
    fun getDateData(date: String) : LiveData<List<RecordEntity>>

    // data 저장 - 중복 값 충돌 발생 시 새로 들어온 데이터로 교체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveData(recordEntity: RecordEntity): Long

    // data 수정
    @Update
    suspend fun update(recordEntity: RecordEntity)

    // data 삭제
    @Delete
    suspend fun deleteRecord(recordEntity: RecordEntity)

    // 저장된 date 값 가져오기
    @Query("SELECT DISTINCT date FROM record_model")
    suspend fun getDate(): LiveData<List<String>>

    // date에 대한 uriList 가져오기
    @Query("SELECT uriList FROM record_model WHERE 'date' = :date")
    suspend fun getDateUriList(date: String): LiveData<List<String>>

    // 전체 삭제
    @Query("DELETE FROM record_model")
    suspend fun deleteAll()

}