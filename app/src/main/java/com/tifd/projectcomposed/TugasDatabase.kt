package com.tifd.projectcomposed.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TugasEntity::class], version = 2, exportSchema = false)
abstract class TugasDatabase : RoomDatabase() {
    abstract fun tugasDao(): TugasDao

    companion object {
        @Volatile
        private var INSTANCE: TugasDatabase? = null

        fun getDatabase(context: Context): TugasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TugasDatabase::class.java,
                    "tugas_database"
                )
                    .fallbackToDestructiveMigration() // Resets database if migration is not provided
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}