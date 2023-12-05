package com.hoon.tourinkorea.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bookmark_posts")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val post: Post,
    @ColumnInfo(name = "added_date") val addedDate: String,
)