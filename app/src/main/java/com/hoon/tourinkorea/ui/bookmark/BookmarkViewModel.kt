package com.hoon.tourinkorea.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoon.tourinkorea.data.model.BookmarkEntity
import com.hoon.tourinkorea.data.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: BookmarkRepository) : ViewModel() {

    private val _items = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    val items: StateFlow<List<BookmarkEntity>> = _items

    fun loadArticle() {
        viewModelScope.launch {
            val posts = repository.getAll()
            _items.value = posts
        }
    }
}