package com.ivan.work.newsapp.network.news.models

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String,
    @SerializedName("isFavorite") var isFavorite: Boolean = false
)
