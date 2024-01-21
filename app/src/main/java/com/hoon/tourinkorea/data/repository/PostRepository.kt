package com.hoon.tourinkorea.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.data.source.PostDataSource
import com.hoon.tourinkorea.network.ApiResponse
import com.hoon.tourinkorea.network.ApiResultSuccess
import com.hoon.tourinkorea.network.onError
import com.hoon.tourinkorea.network.onException
import com.hoon.tourinkorea.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class PostRepository @Inject constructor(private val postDataSource: PostDataSource) {

    fun getPosts(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit,
    ): Flow<Map<String, Post>> = flow {
        val response = postDataSource.getPosts()
        response.onSuccess { data ->
            emit(data)
            data.values.forEach { post ->
                val downloadUrls = getDownloadUrls(post.storageUriList)
                post.downloadUrls = downloadUrls
            }
        }.onError { code, message ->
            onError("code: $code, message: $message")
        }.onException {
            onError(it.message)
        }
    }.onCompletion {
        onComplete()
    }.flowOn(Dispatchers.Default)


    fun createPost(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit,
        title: String,
        location: String,
        description: String,
        imageList: List<Uri>,
    ): Flow<ApiResponse<Map<String, String>>> = flow {
        val storageUriList = uploadImage(imageList)
        val publishedAt = LocalDateTime.now().toString()
        val auth = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token
        val post = Post(title, location, description, storageUriList, publishedAt)

        val response = postDataSource.createPost(auth.toString(), post)
        response.onSuccess { data ->
            emit(ApiResultSuccess(data))
        }.onError { code, message ->
            onError("code: $code, message: $message")
        }.onException {
            onError(it.message)
        }
    }.onCompletion {
        onComplete()
    }.flowOn(Dispatchers.Default)


    private suspend fun uploadImage(imageList: List<Uri>): List<String> {
        return postDataSource.uploadImage(imageList)
    }

    suspend fun getDownloadUrls(storageUriList: List<String>): List<String> {
        val storageRef = FirebaseStorage.getInstance().reference
        return storageUriList.map { storageUri ->
            val ref = storageRef.child(storageUri)
            ref.downloadUrl.await().toString()
        }
    }
}

