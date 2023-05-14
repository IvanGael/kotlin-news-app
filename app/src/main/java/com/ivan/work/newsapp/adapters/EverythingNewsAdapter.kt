package com.ivan.work.newsapp.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.work.newsapp.R
import com.ivan.work.newsapp.SingleNewsActivity
import com.ivan.work.newsapp.network.news.models.Article
import java.text.SimpleDateFormat
import java.util.*

class EverythingNewsAdapter(
    var values : List<Article>
) : RecyclerView.Adapter<EverythingNewsAdapter.NewsViewHolder>(), Filterable {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var articleTitle: TextView
        var articleDescription: TextView
        var articleImage: ImageView
        var articlePublishedAt: TextView
        var imgInfo: ImageView
        var imgShare: ImageView
        var imgSave: ImageView

        private val sharedPreferences: SharedPreferences =
            itemView.context.getSharedPreferences("cached_data", Context.MODE_PRIVATE)

        private var favoritesNews: MutableList<Article>

        init {
            articleTitle = itemView.findViewById(R.id.articleTitle)
            articleDescription = itemView.findViewById(R.id.articleDescription)
            articleImage = itemView.findViewById(R.id.articleImage)
            articlePublishedAt = itemView.findViewById(R.id.articlePublishedAt)
            imgInfo = itemView.findViewById(R.id.img_info)
            imgShare = itemView.findViewById(R.id.img_share)
            imgSave = itemView.findViewById(R.id.img_save)

            // Load favorites from shared preferences
            val cachedData = sharedPreferences.getString("favourite_news", null)
            favoritesNews = if (cachedData != null) {
                Gson().fromJson(cachedData, object : TypeToken<MutableList<Article>>() {}.type)
            } else {
                mutableListOf()
            }
        }

        fun bind(article: Article){
            articleTitle.text = article.title
            articleDescription.text = article.description

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormatTime = SimpleDateFormat("HH:mm", Locale.getDefault())

            val date = inputFormat.parse(article.publishedAt)
            val formattedDate = outputFormatDate.format(date)
            val formattedTime = outputFormatTime.format(date)

            articlePublishedAt.text = "Publié le : $formattedDate à $formattedTime"

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .into(articleImage)

            if(articleImage.drawable == null){
                articleImage.setImageResource(R.drawable.image_not_loaded)
            }


            if (favoritesNews.any { it.title == article.title }) {
                imgSave.setImageResource(R.drawable.favorite)
            } else {
                imgSave.setImageResource(R.drawable.not_favorite)
            }

            imgSave.setOnClickListener {
                article.isFavorite = !article.isFavorite

                if (favoritesNews.contains(article)) {
                    imgSave.setImageResource(R.drawable.not_favorite)
                    favoritesNews.remove(article)
                    sharedPreferences.edit().putString("favourite_news", Gson().toJson(favoritesNews)).apply()
                    // Remove from favorites list
                    /*if (favoritesNews.contains(article)) {
                        favoritesNews.remove(article)
                        sharedPreferences.edit().putString("favourite_news", Gson().toJson(favoritesNews)).apply()
                    }*/
                } else {
                    imgSave.setImageResource(R.drawable.favorite)
                    favoritesNews.add(article)
                    sharedPreferences.edit().putString("favourite_news", Gson().toJson(favoritesNews)).apply()
                    // Add to favorites list
                    /*if (!favoritesNews.contains(article)) {
                        favoritesNews.add(article)
                        sharedPreferences.edit().putString("favourite_news", Gson().toJson(favoritesNews)).apply()
                    }*/
                }

                notifyDataSetChanged()
            }

            imgShare.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, article.url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent,article.title)
                itemView.context.startActivity(shareIntent)
            }

            imgInfo.setOnClickListener {
                Intent(itemView.context, SingleNewsActivity::class.java).also {
                    sharedPreferences.edit().putString("single_news", Gson().toJson(article)).apply()
                    itemView.context.startActivity(it)
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val eltView = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent, false)

        return NewsViewHolder(eltView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = values[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int  = values.size

    fun setArticles(articles: List<Article>) {
        values = articles
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Article>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(values)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (article in values) {
                        if (article.title.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(article)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                values = results?.values as List<Article>
                notifyDataSetChanged()
            }
        }
    }


}