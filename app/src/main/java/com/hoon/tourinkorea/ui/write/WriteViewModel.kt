package com.hoon.tourinkorea.ui.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hoon.tourinkorea.data.post.Post
import com.hoon.tourinkorea.data.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    private val selectedImageUris = mutableListOf<Uri>()

    fun createPost(title: String, location: String, description: String) {
        viewModelScope.launch {
            val storageUriList = postRepository.uploadImage(selectedImageUris)
            val publishedAt = LocalDateTime.now().toString()
            val auth = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token
            val post = Post(title, location, description, storageUriList, publishedAt)
            postRepository.createPost(auth, post)
        }
    }

    fun updateSelectedImages(uris: List<Uri>) {
        selectedImageUris.clear()
        selectedImageUris.addAll(uris)
    }
}
