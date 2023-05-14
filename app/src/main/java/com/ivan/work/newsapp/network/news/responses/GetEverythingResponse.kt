package com.ivan.work.newsapp.network.news.responses

import com.google.gson.annotations.SerializedName
import com.ivan.work.newsapp.network.news.models.Article

data class GetEverythingResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>
)