package com.hoon.tourinkorea.di

import android.content.Context
import com.hoon.tourinkorea.data.source.AppDatabase
import com.hoon.tourinkorea.ui.bookmark.BookmarkPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BookmarkDatabaseModule {

    @Provides
    fun provideBookmarkDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object BookmarkDaoModule {

    @Provides
    fun provideBookmarkDao(database: AppDatabase): BookmarkPostDao {
        return database.bookmarkPostDao()
    }
}
