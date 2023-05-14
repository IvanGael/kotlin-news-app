package com.ivan.work.newsapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ivan.work.newsapp.network.news.models.Article
import java.text.SimpleDateFormat
import java.util.*

class SingleNewsActivity : AppCompatActivity() {

    private lateinit var txtSingleNewsTitle: TextView
    private lateinit var txtSingleNewsSource: TextView
    private lateinit var txtSingleNewsPubDate: TextView
    private lateinit var imgSingleNews: ImageView
    private lateinit var txtSingleNewsDesc: TextView
    private lateinit var txtSingleNewsContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_news)

        initialize()

        val cachedData = getSharedPreferences("cached_data", Context.MODE_PRIVATE).getString("single_news", null)
        if(cachedData != null){
            val data = Gson().fromJson(cachedData, Article::class.java)
            txtSingleNewsTitle.text = data.title
            txtSingleNewsSource.text = "source : ${data.source.name}"

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormatTime = SimpleDateFormat("HH:mm", Locale.getDefault())

            val date = inputFormat.parse(data.publishedAt)
            val formattedDate = outputFormatDate.format(date)
            val formattedTime = outputFormatTime.format(date)

            txtSingleNewsPubDate.text = "publié le $formattedDate à $formattedTime"

            Glide.with(this)
                .load(data.urlToImage)
                .into(imgSingleNews)

            txtSingleNewsDesc.text = data.description
            txtSingleNewsContent.text = data.content
        }
    }

    private fun initialize(){
        txtSingleNewsTitle = findViewById(R.id.txt_single_news_title)
        txtSingleNewsSource = findViewById(R.id.txt_single_news_source)
        txtSingleNewsPubDate = findViewById(R.id.txt_single_news_pub_date)
        imgSingleNews = findViewById(R.id.img_single_news)
        txtSingleNewsDesc = findViewById(R.id.txt_single_news_desc)
        txtSingleNewsContent = findViewById(R.id.txt_single_news_content)
    }
}