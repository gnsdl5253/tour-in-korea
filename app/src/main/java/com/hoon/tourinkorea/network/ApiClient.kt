package com.hoon.tourinkorea.network

import com.hoon.tourinkorea.data.post.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiClient {

    @GET("posts.json")
    suspend fun getPosts(
        @Query("auth") auth: String?
    ): Response<Map<String, Post>>

    @POST("posts.json")
    suspend fun createPost(
        @Query("auth") auth: String?,
        @Body post: Post,
    )
}