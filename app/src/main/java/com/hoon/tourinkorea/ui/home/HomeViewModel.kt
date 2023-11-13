package com.hoon.tourinkorea.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.hoon.tourinkorea.data.post.Post
import com.hoon.tourinkorea.data.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val  postRepository: PostRepository
): ViewModel() {

    private val _items = MutableLiveData<List<Post>>()
    val items: LiveData<List<Post>> = _items

    fun getPost() {
        viewModelScope.launch {
            try {
                val postList = postRepository.getPosts()
                _items.value = postList?.map {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val ref = storageRef.child(it.storageUriList.first())
                    var downloadUri = ""
                    ref.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            downloadUri = task.result.toString()
                            Log.d("TAG", "downloadUri: $downloadUri")
                        } else {
                            Log.e("TAG", "Error downloading file: ${task.exception?.message}")
                        }
                    }.await()
                    Post(it.title, it.location, it.description, listOf(downloadUri), it.publishedAt)
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error getting posts: ${e.message}")
            }
        }
    }
}