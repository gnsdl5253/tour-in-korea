package com.hoon.tourinkorea.ui.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiClient {

    @GET("v2/local/search/keyword.json")
    fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String

    ): Call<ResultSearchKeyword>
}
