package com.example.mvvm2.room

import android.app.Application
import androidx.room.Room

class GlobalApplication: Application() {
    companion object {
        lateinit var appInstance: GlobalApplication
            private set
        lateinit var recordDatabaseInstance: RecordDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appInstance = this

        recordDatabaseInstance = Room.databaseBuilder(
            appInstance.applicationContext,
            RecordDatabase::class.java, "record.db"
        )
            .fallbackToDestructiveMigration() // DB version 달라졌을 경우 데이터베이스 초기화
            .allowMainThreadQueries() // 메인 스레드에서 접근 허용
            .build()
    }
}