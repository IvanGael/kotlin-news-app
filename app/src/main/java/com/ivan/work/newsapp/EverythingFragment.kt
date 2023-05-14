package com.ivan.work.newsapp

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ivan.work.newsapp.adapters.EverythingNewsAdapter
import com.ivan.work.newsapp.adapters.TopNewsAdapter
import com.ivan.work.newsapp.network.ApiClient
import com.ivan.work.newsapp.network.news.NewsService
import com.ivan.work.newsapp.network.news.responses.GetEverythingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EverythingFragment : Fragment() {

    private lateinit var searchToolbar: Toolbar
    private lateinit var everythingRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var newsService: NewsService
    private lateinit var adapter: EverythingNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_everything, container, false)

        searchToolbar = view.findViewById(R.id.toolbar)
        everythingRecyclerView = view.findViewById(R.id.everythingRecyclerView)
        searchEditText = view.findViewById(R.id.search_edittext)
        searchIcon = view.findViewById(R.id.search_icon)
        everythingRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = EverythingNewsAdapter(listOf())
        everythingRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsService = ApiClient.newsService

        /*// onChange event on searchEditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ne rien faire avant le changement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Action à effectuer à chaque changement de texte dans l'EditText
            }

            override fun afterTextChanged(s: Editable?) {
                // Ne rien faire après le changement
            }
        })*/

        searchIcon.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                val call = newsService.getEverything(
                    query,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    getString(R.string.news_api_key)
                )

                call.enqueue(object : Callback<GetEverythingResponse> {
                    override fun onResponse(
                        call: Call<GetEverythingResponse>,
                        response: Response<GetEverythingResponse>
                    ) {
                        if (response.isSuccessful) {
                            val newsResponse = response.body()

                            if (newsResponse != null) {
                                adapter.setArticles(newsResponse.articles)
                                Toast.makeText(context, "${adapter.itemCount} articles found!", Toast.LENGTH_LONG).show()
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
            } else {
                Toast.makeText(context, "searchEditText cannot be empty", Toast.LENGTH_LONG).show()
            }
        }
    }

}