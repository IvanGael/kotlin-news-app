package com.ivan.work.newsapp.network.news.models

data class ApiError(
    val status: String,
    val code: String,
    val message: String
)