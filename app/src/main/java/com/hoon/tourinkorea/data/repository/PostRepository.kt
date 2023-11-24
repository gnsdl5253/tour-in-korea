package com.hoon.tourinkorea.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.network.ApiResponse
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class PostRepository @Inject constructor(private val postDataSource: PostDataSource) {

    suspend fun getPosts(): ApiResponse<Map<String, Post>> {
        return postDataSource.getPosts()
    }

    suspend fun createPost(title: String, location: String, description: String, imageList: List<Uri>) {
        val storageUriList = uploadImage(imageList)
        val publishedAt = LocalDateTime.now().toString()
        val auth = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token
        val post = Post(title, location, description, storageUriList, publishedAt)
        postDataSource.createPost(auth.toString(), post)
    }

    private suspend fun uploadImage(imageList: List<Uri>): List<String> {
        return postDataSource.uploadImage(imageList)
    }
}