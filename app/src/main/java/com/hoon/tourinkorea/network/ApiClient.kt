package com.hoon.tourinkorea.network

import com.hoon.tourinkorea.data.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiClient {

    @GET("posts.json")
    suspend fun getPosts(
        @Query("auth") auth: String?
    ): ApiResponse<Map<String, Post>>

    @POST("posts.json")
    suspend fun createPost(
        @Query("auth") auth: String?,
        @Body post: Post,
    ): ApiResponse<Map<String,String>>
}