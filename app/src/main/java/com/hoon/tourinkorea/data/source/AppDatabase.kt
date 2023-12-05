package com.hoon.tourinkorea.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoon.tourinkorea.data.model.BookmarkEntity
import com.hoon.tourinkorea.data.model.Converters
import com.hoon.tourinkorea.ui.bookmark.BookmarkPostDao

@Database(entities = [BookmarkEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkPostDao(): BookmarkPostDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "tour-local-db"
            ).build().also {
                instance = it
            }
        }
    }
}