package com.ivan.work.newsapp

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ivan.work.newsapp.adapters.TopNewsAdapter
import com.ivan.work.newsapp.network.ApiClient
import com.ivan.work.newsapp.network.countries.models.Country
import com.ivan.work.newsapp.network.news.NewsService
import com.ivan.work.newsapp.network.news.responses.GetEverythingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopHeadlinesFragment : Fragment() {

    private lateinit var topHeadlinesRecyclerView: RecyclerView

    private lateinit var newsService: NewsService

    private lateinit var country: Country



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top_headlines, container, false)

        topHeadlinesRecyclerView = view.findViewById(R.id.topHeadlinesRecyclerView)
        topHeadlinesRecyclerView.layoutManager = LinearLayoutManager(context)
        //affichage sur deux colonnes
        // topHeadlinesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        newsRequest()

        return view
    }


    private fun newsRequest() {
        newsService = ApiClient.newsService

        //val country = "US"

        val cachedData = context?.getSharedPreferences("cached_data", Context.MODE_PRIVATE)?.getString("favourite_country", null)

        if(cachedData != null){
            country = Gson().fromJson(cachedData, Country::class.java)

            val call = newsService.getArticles(country.cca2, getString(R.string.news_api_key))

            call.enqueue(object : Callback<GetEverythingResponse> {
                override fun onResponse(call: Call<GetEverythingResponse>, response: Response<GetEverythingResponse>) {
                    if (response.isSuccessful) {
                        val newsResponse = response.body()

                        if (newsResponse != null) {
                            val adapter = TopNewsAdapter(newsResponse.articles)
                            topHeadlinesRecyclerView.adapter = adapter
                            adapter.setArticles(newsResponse.articles)
                        } else {
                            Toast.makeText(context, "response is null", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(context, "response is unsuccessfull", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<GetEverythingResponse>, t: Throwable) {
                    // Handle API request failure
                    Log.e(ContentValues.TAG, "API request failed: ${t.message}")
                    Toast.makeText(context, "API request failed: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }



}