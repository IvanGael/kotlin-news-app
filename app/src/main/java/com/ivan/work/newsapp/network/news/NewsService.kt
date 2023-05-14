package com.ivan.work.newsapp.network.news

import com.ivan.work.newsapp.network.news.models.Article
import com.ivan.work.newsapp.network.news.responses.GetEverythingResponse
import com.ivan.work.newsapp.network.news.responses.GetTopHeadlinesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<GetTopHeadlinesResponse>

    @GET("everything")
    fun getEverything(
        @Query("q") searchTerm: String,
        @Query("sources") sources: String? = null,
        @Query("domains") domains: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int? = null,
        @Query("apiKey") apiKey: String
    ): Call<GetEverythingResponse>

    @GET("top-headlines")
    fun getArticles(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<GetEverythingResponse>
}