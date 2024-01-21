package com.hoon.tourinkorea.network

import com.hoon.tourinkorea.data.model.ResultSearchKeyword
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiClient {

    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,

        ): ApiResponse<ResultSearchKeyword>
}
