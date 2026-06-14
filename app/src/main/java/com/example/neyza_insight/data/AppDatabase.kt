package com.example.neyza_insight.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.neyza_insight.data.dao.KelahiranDao
import com.example.neyza_insight.data.dao.KematianDao
import com.example.neyza_insight.data.entity.KelahiranEntity
import com.example.neyza_insight.data.entity.KematianEntity

@Database(
    entities = [KelahiranEntity::class, KematianEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun kelahiranDao(): KelahiranDao
    abstract fun kematianDao(): KematianDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neyza_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
