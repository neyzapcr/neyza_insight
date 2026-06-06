package com.example.neyza_insight.data.api

import com.example.neyza_insight.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/news/all")
    suspend fun getBeritaKependudukan(
        @Query("search") query: String = "kependudukan indonesia",
        @Query("language") language: String = "id",
        @Query("limit") limit: Int = 3,
        @Query("categories") categories: String = "general",
        @Query("api_token") apiKey: String = "lQAp97dQbZQfBfQAy1UddLc8cPGbIDBDBFnmwCSA"
    ): NewsResponse
}