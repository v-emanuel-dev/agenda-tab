package com.ivip.agendatab.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.ivip.agendatab.data.local.entities.DailyEntryEntity
@Database(
    entities = [DailyEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dailyEntryDao(): DailyEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agenda_tab_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}