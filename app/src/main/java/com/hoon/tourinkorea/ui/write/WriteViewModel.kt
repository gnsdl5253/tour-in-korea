package com.hoon.tourinkorea.ui.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoon.tourinkorea.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris

    fun createPost() {
        viewModelScope.launch {
            postRepository.createPost(
                onComplete = { },
                onError = { },
                _title.value,
                _location.value,
                _description.value,
                _selectedImageUris.value
            ).collect { }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateSelectedImages(uris: List<Uri>) {
        _selectedImageUris.value = uris
    }

    fun removeSelectedImage(position: Int) {
        val updatedList = _selectedImageUris.value?.toMutableList() ?: mutableListOf()
        if (position in 0 until updatedList.size) {
            updatedList.removeAt(position)
            _selectedImageUris.value = updatedList
        }
    }
}