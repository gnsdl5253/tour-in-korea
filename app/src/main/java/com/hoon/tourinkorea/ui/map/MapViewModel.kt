package com.hoon.tourinkorea.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoon.tourinkorea.data.model.ResultSearchKeyword
import com.hoon.tourinkorea.data.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {

    private val _items = MutableStateFlow<ResultSearchKeyword?>(null)
    val items: StateFlow<ResultSearchKeyword?> = _items

    private var searchJob: Job? = null

    fun searchKeyword(keyword: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            Log.d("MapViewModel", "Search keyword initiated: $keyword")
            mapRepository.searchKeyword(
                onComplete = { Log.d("MapViewModel", "Search completed") },
                onError = { _items.value = null },
                keyword = keyword
            ).collect { resultSearchKeyword ->
                _items.value = resultSearchKeyword
                Log.d("MapViewModel", "Search result received: $resultSearchKeyword")
            }
        }
    }
}