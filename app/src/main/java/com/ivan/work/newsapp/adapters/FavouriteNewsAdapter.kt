package com.ivan.work.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ivan.work.newsapp.R
import com.ivan.work.newsapp.network.news.models.Article

class FavouriteNewsAdapter(
    private val ctxt: Context,
    private val values : List<Article>
): ArrayAdapter<Article>(ctxt,0,values) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View = LayoutInflater.from(ctxt).inflate(R.layout.item_favourite_news,parent,false)

        val txtFavNewsTitle = itemView.findViewById<TextView>(R.id.txt_fav_news_title)
        val txtFavNewsDesc = itemView.findViewById<TextView>(R.id.txt_fav_news_desc)
        val imgViewFavNews = itemView.findViewById<ImageView>(R.id.img_view_fav_news)

        val article = values[position]

        txtFavNewsTitle.text = article.title
        txtFavNewsDesc.text = article.description

        Glide.with(ctxt)
            .load(article.urlToImage)
            .into(imgViewFavNews)

        if(imgViewFavNews.drawable == null){
            imgViewFavNews.setImageResource(R.drawable.image_not_loaded)
        }

        return itemView
    }
}