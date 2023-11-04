package com.hoon.tourinkorea.data.post

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.hoon.tourinkorea.network.ApiClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(private val apiClient: ApiClient) : PostDataSource {

    override suspend fun getPosts(): List<Post>? {
        val auth = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token
        return apiClient.getPosts(auth).body()?.values?.toList()
    }

    override suspend fun uploadImage(imageList: List<Uri>): List<String> {

        val storageRef = FirebaseStorage.getInstance().reference
        return imageList.map { uri ->
            val location = "image/${uri.lastPathSegment}_${System.currentTimeMillis()}"
            val imageRef = storageRef.child(location)
            imageRef.putFile(uri).await()
            location
        }
    }

    suspend fun createPost(auth: String, post: Post){
        apiClient.createPost(auth, post)
    }
}