package com.ivan.work.newsapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.ivan.work.newsapp.adapters.CountriesAdapter
import com.ivan.work.newsapp.network.ApiClient
import com.ivan.work.newsapp.network.countries.CountriesService
import com.ivan.work.newsapp.network.countries.models.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var txtCurrentDate: TextView

    private lateinit var countriesService: CountriesService

    private val topHeadlinesFragment = TopHeadlinesFragment()
    private val everythingFragment = EverythingFragment()

    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = topHeadlinesFragment


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*val country_code = intent.getStringExtra("country_code")
        Log.e(TAG, "country_code : $country_code")*/

        supportActionBar?.title = "Top news"

        initialize()

        //set current date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        txtCurrentDate.text = currentDate

        manageFragment()
        setupNavigation()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemFavoris -> {
                Intent(this@HomeActivity, FavouriteNewsActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.itemConfig -> {
                Intent(this@HomeActivity, AppSettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun initialize(){
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        frameLayout = findViewById(R.id.frame_layout)
        txtCurrentDate = findViewById(R.id.txt_current_date)
    }

    private fun manageFragment(){
        fragmentManager.beginTransaction().apply {
            add(R.id.frame_layout, everythingFragment, "2")
            hide(everythingFragment)
            add(R.id.frame_layout, topHeadlinesFragment, "1")
            commit()
        }
    }


    private fun setupNavigation(){
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_top_headlines -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment)
                        show(topHeadlinesFragment)
                        commit()
                    }
                    activeFragment = topHeadlinesFragment
                    supportActionBar?.title = "Top news"
                    true
                }
                R.id.navigation_everything -> {
                    fragmentManager.beginTransaction().apply {
                        hide(activeFragment)
                        show(everythingFragment)
                        commit()
                    }
                    activeFragment = everythingFragment
                    supportActionBar?.title = "Search news"
                    true
                }
                else -> false
            }
        }
    }


    /*@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun countriesRequest(){
        countriesService = ApiClient.countriesService

        // Load the cached data from SharedPreferences
        val cachedData = getSharedPreferences("cached_data", Context.MODE_PRIVATE).getString("countries_data", null)

        if (cachedData != null) {
            // Data is cached, use it instead of making an API call
            val countriesResponse = Gson().fromJson(cachedData, Array<Country>::class.java)

            val adapter = CountriesAdapter(this@HomeActivity, countriesResponse.toList().sortedBy { it.name.common })
            countriesSpinner.adapter = adapter

            countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedItem = p0?.getItemAtPosition(p2) as Country
                    //Toast.makeText(this@HomeActivity, "selectedItem : ${selectedItem.name.common}", Toast.LENGTH_LONG).show()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
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

                        val adapter = countriesResponse?.let { CountriesAdapter(this@HomeActivity, it.sortedBy { country -> country.name.common  }) }
                        countriesSpinner.adapter = adapter

                        countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                val selectedItem = p0?.getItemAtPosition(p2) as Country
                                Toast.makeText(this@HomeActivity, "selectedItem : ${selectedItem.name.common}", Toast.LENGTH_LONG).show()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e(TAG, "API request failed: ${t.message}")
                }

            })
        }
    }*/



}