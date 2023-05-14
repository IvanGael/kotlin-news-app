package com.ivan.work.newsapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.work.newsapp.adapters.FavouriteNewsAdapter
import com.ivan.work.newsapp.network.news.models.Article

class FavouriteNewsActivity : AppCompatActivity() {

    private lateinit var listViewFavoriteNews: ListView

    private lateinit var favouriteNewsAdapter: FavouriteNewsAdapter

    private lateinit var txtFavoriteDefault: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_news)

        initialize()

        val cachedData = getSharedPreferences("cached_data", Context.MODE_PRIVATE).getString("favourite_news", null)
        if(cachedData != null){
            val data: MutableList<Article> = Gson().fromJson(cachedData, object : TypeToken<MutableList<Article>>() {}.type)
            favouriteNewsAdapter = FavouriteNewsAdapter(this,data)
            listViewFavoriteNews.adapter = favouriteNewsAdapter
            favouriteNewsAdapter.notifyDataSetChanged()
        }

        if(listViewFavoriteNews.adapter.isEmpty){
            txtFavoriteDefault.visibility = View.VISIBLE
        }
    }


    private fun initialize(){
        listViewFavoriteNews = findViewById(R.id.list_view_favorite_news)
        txtFavoriteDefault = findViewById(R.id.txt_favorite_default_text)
    }
}