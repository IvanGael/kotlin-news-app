package com.ivan.work.newsapp.adapters

import android.content.Context
import android.database.DataSetObserver
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.ivan.work.newsapp.R
import com.ivan.work.newsapp.network.countries.models.Country

class CountriesAdapter(
    val ctxt: Context,
    val values : List<Country>
): ArrayAdapter<Country>(ctxt, 0, values){

     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View = LayoutInflater.from(ctxt).inflate(R.layout.item_country, parent, false)

        val countryFlag = itemView.findViewById<ImageView>(R.id.country_flag)
        val countryName = itemView.findViewById<TextView>(R.id.country_name)

        val country = values[position]

        countryName.text = country.name.common
        Glide.with(ctxt)
            .load(country.flags.png)
            .into(countryFlag)

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View = LayoutInflater.from(ctxt).inflate(R.layout.item_country, parent, false)

        val countryFlag = itemView.findViewById<ImageView>(R.id.country_flag)
        val countryName = itemView.findViewById<TextView>(R.id.country_name)

        val country = values[position]

        countryName.text = country.name.common
        Glide.with(ctxt)
            .load(country.flags.png)
            .into(countryFlag)

        return itemView
    }

}