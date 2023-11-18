package com.hoon.tourinkorea.data.post

import android.net.Uri
import com.hoon.tourinkorea.network.ApiResponse

interface PostDataSource {

    suspend fun getPosts(): ApiResponse<Map<String, Post>>

    suspend fun createPost(auth: String, post: Post)

    suspend fun uploadImage(imageList: List<Uri>): List<String>
}
