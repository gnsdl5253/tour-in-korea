package com.hoon.tourinkorea.data.model

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val postAdapter: JsonAdapter<Post> = moshi.adapter(Post::class.java)

    @TypeConverter
    fun postToString(post: Post): String? {
        return postAdapter.toJson(post)
    }

    @TypeConverter
    fun stringToPost(postJson: String?): Post? {
        return postAdapter.fromJson(postJson ?: "")
    }
}
