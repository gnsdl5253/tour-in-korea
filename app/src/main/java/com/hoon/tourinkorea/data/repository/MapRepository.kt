package com.hoon.tourinkorea.data.repository

import android.util.Log
import com.hoon.tourinkorea.network.onError
import com.hoon.tourinkorea.network.onException
import com.hoon.tourinkorea.network.onSuccess
import com.hoon.tourinkorea.ui.map.ResultSearchKeyword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class MapRepository @Inject constructor(private val mapDataSource: MapDataSource) {

    fun searchKeyword(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit,
        keyword: String,
    ): Flow<ResultSearchKeyword> = flow {

        val call = mapDataSource.searchKeyword(keyword)
        call.onSuccess {
            Log.d("MapRepository", "Search success: $it")
            emit(it)
        }.onError { code, message ->
            Log.e("MapRepository", "Search error: code $code, message: $message")
            onError("code: $code, message: $message")
        }.onException {
            Log.e("MapRepository", "Search exception: ${it.message}")
            onError(it.message)
        }
    }.onCompletion {
        Log.d("MapRepository", "Search completed")
        onComplete()
    }
}