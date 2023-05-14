package com.ivan.work.newsapp.network.news.responses

import com.google.gson.annotations.SerializedName
import com.ivan.work.newsapp.network.news.models.Article
import com.ivan.work.newsapp.network.news.models.Source

data class GetTopHeadlinesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("sources") val sources: List<Source>
)

