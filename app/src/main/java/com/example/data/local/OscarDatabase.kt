package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VideoEntity::class, CommentEntity::class], version = 1, exportSchema = false)
abstract class OscarDatabase : RoomDatabase() {
    abstract fun oscarDao(): OscarDao

    companion object {
        @Volatile
        private var INSTANCE: OscarDatabase? = null

        fun getDatabase(context: Context): OscarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OscarDatabase::class.java,
                    "oscartuber_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
