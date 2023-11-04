package com.hoon.tourinkorea.data.post

import android.net.Uri
import javax.inject.Inject

class PostRepository @Inject constructor(private val postRemoteDataSource: PostRemoteDataSource) {

    suspend fun getPosts(): List<Post>? {
        return postRemoteDataSource.getPosts()
    }

    suspend fun createPost(auth: String?, post: Post) {
        postRemoteDataSource.createPost(auth.toString(), post)
    }

    suspend fun uploadImage(imageList: List<Uri>): List<String> {
        return postRemoteDataSource.uploadImage(imageList)
    }
}