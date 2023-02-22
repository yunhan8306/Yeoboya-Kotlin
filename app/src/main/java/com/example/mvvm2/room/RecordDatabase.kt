package com.example.mvvm2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvm2.entity.RecordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@Database(entities = [RecordEntity::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDAO(): RecordDAO

    private class RecordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.recordDAO())
                }
            }
        }

        suspend fun populateDatabase(recordDAO: RecordDAO) {
            // Delete all content here.
//            recordDAO.deleteAll()

            // Add sample words.
            var record = RecordEntity(0,"test1","content1","2023-01-01","12:30","https://assets.vogue.com/photos/63c8a2077022d35c31c766ee/master/w_1920,c_limit/00001-lemaire-fall-2023-menswear-credit-lena-emery.jpg")
            var record2 = RecordEntity(0,"test2","content2","2023-02-02","13:30","https://assets.vogue.com/photos/63c2e6383b8c841452deb19b/master/w_1920,c_limit/00001-our-legacy-fall-2023-ready-to-wear-credit-isak-berglund-mattsson-marn.jpg")
            recordDAO.saveData(record)
            recordDAO.saveData(record2)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RecordDatabase ?= null

        fun getDatabase(context: Context, scope: CoroutineScope): RecordDatabase {
            // 만약에 INSTANCE가 null이 아니면 리턴하고 널이면 데이터베이스를 만들어라.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    "word_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(RecordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}