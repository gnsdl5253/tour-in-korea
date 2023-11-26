package com.hoon.tourinkorea.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _items = MutableStateFlow<List<Post>>(emptyList())
    val items: StateFlow<List<Post>> = _items

    fun getPost() {
        viewModelScope.launch {
            try {
                postRepository.getPosts(
                    onComplete = { },
                    onError = { _items.value = emptyList() },
                ).collect { data ->

                    val postList = data.values

                    _items.value = postList.map {
                        val downloadUrls = postRepository.getDownloadUrls(it.storageUriList)

                        Post(
                            it.title,
                            it.location,
                            it.description,
                            downloadUrls,
                            it.publishedAt
                        )
                    }

                }
            } catch (e: Exception) {
                Log.e("TAG", "게시물 가져오기 오류: ${e.message}")
            }
        }
    }
}