package com.ivan.work.newsapp

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.ivan.work.newsapp.adapters.CountriesAdapter
import com.ivan.work.newsapp.network.ApiClient
import com.ivan.work.newsapp.network.countries.CountriesService
import com.ivan.work.newsapp.network.countries.models.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var countriesSpinner: Spinner

    private lateinit var countriesService: CountriesService

    private lateinit var favouriteCountry: Country

    private lateinit var saveButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)

        supportActionBar?.title = "Settings"

        initialize()
        countriesRequest()
        saveButton.isEnabled = false
    }

    override fun onStart() {
        super.onStart()

        saveButton.setOnClickListener {
            if(favouriteCountry != null) {
                getSharedPreferences("cached_data", Context.MODE_PRIVATE).edit().putString("favourite_country", Gson().toJson(favouriteCountry)).apply()
                Toast.makeText(this@AppSettingsActivity, "Preferences saved successfully!",Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun initialize(){
        countriesSpinner = findViewById(R.id.countries_spinner)
        saveButton = findViewById(R.id.save_button)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun countriesRequest(){
        countriesService = ApiClient.countriesService

        // Load the cached data from SharedPreferences
        val cachedData = getSharedPreferences("cached_data", Context.MODE_PRIVATE).getString("countries_data", null)

        if (cachedData != null) {
            // Data is cached, use it instead of making an API call
            val countriesResponse = Gson().fromJson(cachedData, Array<Country>::class.java)

            val adapter = CountriesAdapter(this@AppSettingsActivity, countriesResponse.toList().sortedBy { it.name.common })
            countriesSpinner.adapter = adapter

            countriesSpinner.setSelection(235)
            countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    favouriteCountry = p0?.getItemAtPosition(p2) as Country
                    saveButton.isEnabled = true
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    saveButton.isEnabled = false
                }
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

                        val adapter = countriesResponse?.let {
                            CountriesAdapter(this@AppSettingsActivity, it.sortedBy { country -> country.name.common  })
                        }

                        countriesSpinner.adapter = adapter

                        countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                favouriteCountry = p0?.getItemAtPosition(p2) as Country
                                Toast.makeText(this@AppSettingsActivity, "selectedItem : ${favouriteCountry.name.common}", Toast.LENGTH_LONG).show()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e(ContentValues.TAG, "API request failed: ${t.message}")
                }

            })
        }
    }
}