package com.hoon.tourinkorea.data.source

import android.net.Uri
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.network.ApiResponse

interface PostDataSource {

    suspend fun getPosts(): ApiResponse<Map<String, Post>>

    suspend fun createPost(auth: String, post: Post): ApiResponse<Map<String,String>>

    suspend fun uploadImage(imageList: List<Uri>): List<String>
}
