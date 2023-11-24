package com.hoon.tourinkorea.ui.write

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoon.tourinkorea.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    val title = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val selectedImageUris = mutableListOf<Uri>()

    fun createPost(title: String, location: String, description: String) {
        viewModelScope.launch {
            postRepository.createPost(title, location, description, selectedImageUris)
        }
    }

    fun updateSelectedImages(uris: List<Uri>) {
        selectedImageUris.clear()
        selectedImageUris.addAll(uris)
    }
}
