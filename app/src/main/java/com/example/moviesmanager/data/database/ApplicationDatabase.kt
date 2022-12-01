package com.example.moviesmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesmanager.data.dao.MovieDao
import com.example.moviesmanager.data.entity.MovieEntity

private const val DB_VERSION = 1
private const val DB_NAME = "application_database"

@Database(entities = [MovieEntity::class], version = DB_VERSION)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: ApplicationDatabase? = null

        fun getDatabaseInstance(mContext: Context): ApplicationDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabaseInstance(mContext).also {
                    instance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, ApplicationDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

    }
}