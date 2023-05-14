package com.ivan.work.newsapp.network

import com.ivan.work.newsapp.network.countries.CountriesService
import com.ivan.work.newsapp.network.news.NewsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val NEWS_BASE_URL = "https://newsapi.org/v2/"
        private const val COUNTRIES_BASE_URL = "https://restcountries.com/v3.1/"

        private val newsRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val countriesRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(COUNTRIES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsService: NewsService = newsRetrofit.create(NewsService::class.java)
        val countriesService: CountriesService = countriesRetrofit.create(CountriesService::class.java)
    }
}
