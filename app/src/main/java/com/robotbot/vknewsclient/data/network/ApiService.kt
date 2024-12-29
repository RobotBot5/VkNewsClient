package com.robotbot.vknewsclient.data.network

import com.robotbot.vknewsclient.data.model.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.199&filters=post")
    suspend fun loadWall(
        @Query("access_token") token: String
    ): NewsFeedResponseDto
}