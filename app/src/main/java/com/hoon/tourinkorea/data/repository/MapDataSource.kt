package com.hoon.tourinkorea.data.repository

import com.hoon.tourinkorea.network.ApiResponse
import com.hoon.tourinkorea.ui.map.ResultSearchKeyword

interface MapDataSource {

    suspend fun searchKeyword(keyword: String): ApiResponse<ResultSearchKeyword>
}