package com.nitishsharma.greedygame.api

import com.nitishsharma.greedygame.api.models.News
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/everything")
    suspend fun getNews(@Query("q") search: String, @Query("apiKey") apiKey: String): News
}