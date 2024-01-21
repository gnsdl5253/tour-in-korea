package com.hoon.tourinkorea.data.source

import android.util.Log
import com.hoon.tourinkorea.BuildConfig
import com.hoon.tourinkorea.network.ApiResponse
import com.hoon.tourinkorea.network.ApiResultError
import com.hoon.tourinkorea.network.KakaoApiClient
import com.hoon.tourinkorea.data.model.ResultSearchKeyword
import javax.inject.Inject

class MapRemoteDataSource @Inject constructor(private val apiClient: KakaoApiClient) :
    MapDataSource {

    override suspend fun searchKeyword(keyword: String): ApiResponse<ResultSearchKeyword> {

        return try {
            Log.d("MapRemoteDataSource", "Search keyword: $keyword")

            val response = apiClient.getSearchKeyword(BuildConfig.API_KEY, query = keyword)

            Log.d("MapRemoteDataSource", "API response: $response")

            response
        } catch (e: Exception) {
            Log.e("MapRemoteDataSource", "Exception: ${e.message}", e)
            ApiResultError(0, e.message.toString())
        }
    }
}