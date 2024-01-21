package com.hoon.tourinkorea.data.source

import com.hoon.tourinkorea.network.ApiResponse
import com.hoon.tourinkorea.data.model.ResultSearchKeyword

interface MapDataSource {

    suspend fun searchKeyword(keyword: String): ApiResponse<ResultSearchKeyword>
}