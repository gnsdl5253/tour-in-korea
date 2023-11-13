package com.hoon.tourinkorea.data.post

import android.net.Uri

interface PostDataSource {

    suspend fun getPosts(): List<Post>?

    suspend fun createPost(auth: String, post: Post)

    suspend fun uploadImage(imageList: List<Uri>): List<String>
}
