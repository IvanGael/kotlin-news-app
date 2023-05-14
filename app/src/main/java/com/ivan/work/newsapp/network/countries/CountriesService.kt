package com.ivan.work.newsapp.network.countries

import com.ivan.work.newsapp.network.countries.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CountriesService {
    @GET("all")
    fun getAll(): Call<List<Country>>

    @GET("name/{name}")
    fun getByName(@Path("name") name: String): Call<List<Country>>

    @GET("alpha/{code}")
    fun getByCountryCode(@Path("code") name: String): Call<List<Country>>
}
