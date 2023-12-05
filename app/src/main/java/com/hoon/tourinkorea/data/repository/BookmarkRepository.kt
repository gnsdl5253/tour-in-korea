package com.hoon.tourinkorea.data.repository

import com.hoon.tourinkorea.data.model.BookmarkEntity
import com.hoon.tourinkorea.data.source.AppDatabase
import javax.inject.Inject

class BookmarkRepository @Inject constructor (database: AppDatabase) {

    private val dao = database.bookmarkPostDao()

    suspend fun getAll(): List<BookmarkEntity> {
        return dao.getAll()
    }
}