package com.hoon.tourinkorea.ui.bookmark

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hoon.tourinkorea.data.model.BookmarkEntity


@Dao
interface BookmarkPostDao {
    @Query("SELECT * FROM BOOKMARK_POSTS ORDER BY added_date DESC")
    suspend fun getAll(): List<BookmarkEntity>

    @Insert
    suspend fun insert(savedPost: BookmarkEntity)

    @Delete
    suspend fun delete(savedPost: BookmarkEntity)
}
