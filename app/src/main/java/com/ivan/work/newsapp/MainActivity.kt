package com.ivan.work.newsapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ivan.work.newsapp.HomeActivity
import com.ivan.work.newsapp.R
import com.ivan.work.newsapp.network.ApiClient
import com.ivan.work.newsapp.network.countries.CountriesService
import com.ivan.work.newsapp.network.countries.models.Country
import com.ivan.work.newsapp.network.news.responses.GetTopHeadlinesResponse
import com.ivan.work.newsapp.network.news.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var newsService: NewsService
    private lateinit var countriesService: CountriesService

    private lateinit var countriesGridLayout: GridLayout
    private lateinit var btnGo: Button
    private var selectedImageView: ImageView? = null
    private lateinit var selectedCountry: Country

    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("app_state",Context.MODE_PRIVATE)
        val isAlreadySetCountry = sharedPreferences.getBoolean("isAlreadySetCountry",false)
        if(isAlreadySetCountry){
            Intent(this@MainActivity,HomeActivity::class.java).also {
                startActivity(it)
            }
        }

        countriesGridLayout = findViewById(R.id.countries_grid_layout)
        btnGo = findViewById(R.id.btnGo)


        countriesRequest()
    }

    override fun onStart() {
        super.onStart()

        val intentToHomeActivity = Intent(this, HomeActivity::class.java)
        btnGo.setOnClickListener {
            if(selectedCountry != null) {
                val editor = sharedPreferences.edit()
                editor.putBoolean("isAlreadySetCountry",true)
                editor.apply()
                getSharedPreferences("cached_data", Context.MODE_PRIVATE).edit().putString("favourite_country", Gson().toJson(selectedCountry)).apply()
                startActivity(intentToHomeActivity)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun countriesRequest(){
        countriesService = ApiClient.countriesService

        // Load the cached data from SharedPreferences
        val cachedData = getSharedPreferences("cached_data", Context.MODE_PRIVATE).getString("countries_data", null)

        if (cachedData != null) {
            // Data is cached, use it instead of making an API call
            val countriesResponse = Gson().fromJson(cachedData, Array<Country>::class.java)

            val countriesResponseSorted = countriesResponse?.sortedBy { it.name.common }
            countriesResponseSorted?.forEach { country ->
                val name = country.name.common
                val flagUrl = country.flags.png
                val imageView = ImageView(applicationContext)

                // Load the image using Glide
                Glide.with(this@MainActivity)
                    .load(flagUrl)
                    .into(imageView)
                imageView.isClickable = true
                imageView.setOnClickListener { handleCountry(country, imageView) }

                // Set the width and height of the ImageView
                val layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
                )
                layoutParams.width = 150
                layoutParams.height = 150

                imageView.layoutParams = layoutParams

                countriesGridLayout.addView(imageView)
            }
        } else {
            // Data is not cached, make an API call to fetch the data
            val call = countriesService.getAll()

            call.enqueue(object : Callback<List<Country>> {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    if(response.isSuccessful){
                        val countriesResponse = response.body()

                        // Cache the data using SharedPreferences
                        getSharedPreferences("cached_data", Context.MODE_PRIVATE).edit().putString("countries_data", Gson().toJson(countriesResponse)).apply()

                        val countriesResponseSorted = countriesResponse?.sortedBy { it.name.common }
                        countriesResponseSorted?.forEach { country ->
                            val name = country.name.common
                            val flagUrl = country.flags.png
                            val imageView = ImageView(applicationContext)

                            // Load the image using Glide
                            Glide.with(this@MainActivity)
                                .load(flagUrl)
                                .into(imageView)
                            imageView.isClickable = true
                            imageView.setOnClickListener { handleCountry(country, imageView) }

                            // Set the width and height of the ImageView
                            val layoutParams = GridLayout.LayoutParams(
                                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            )
                            layoutParams.width = 150
                            layoutParams.height = 150

                            imageView.layoutParams = layoutParams

                            countriesGridLayout.addView(imageView)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e(TAG, "API request failed: ${t.message}")
                }

            })
        }
    }


    private fun handleCountry(country: Country, clickedImageView: ImageView?){
        Log.e(TAG, "country : ${country.name.common}")

        if (clickedImageView == this.selectedImageView) {
            btnGo.isEnabled = false
            // Clicked again on the same ImageView, reset all ImageViews to their initial state
            for (i in 0 until countriesGridLayout.childCount) {
                val view = countriesGridLayout.getChildAt(i)
                if (view is ImageView) {
                    view.isClickable = true
                    view.alpha = 1f
                }
            }
            this.selectedImageView = null
        } else {
            // Clicked on a new ImageView, disable other ImageViews
            this.selectedImageView = clickedImageView
            btnGo.isEnabled = true
            for (i in 0 until countriesGridLayout.childCount) {
                val view = countriesGridLayout.getChildAt(i)
                if (view != clickedImageView && view is ImageView) {
                    view.isClickable = false
                    view.alpha = 0.5f
                }
            }
            selectedCountry = country
        }

    }

}