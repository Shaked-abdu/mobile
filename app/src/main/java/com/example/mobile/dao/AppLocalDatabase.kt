package com.example.mobile.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobile.Model.Post
import com.example.mobile.base.MyApplication

@Database(entities = [Post::class], version = 2)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun postDao(): PostDao
}

object AppLocalDatabase {

    val db: AppLocalDbRepository by lazy {

        val context = MyApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "dbFileName.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
