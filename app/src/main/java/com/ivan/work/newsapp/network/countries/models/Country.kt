package com.ivan.work.newsapp.network.countries.models

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("flags") val flags: Flags,
    @SerializedName("postalCode") val postalCode: Any,
    @SerializedName("name") val name: Name,
    @SerializedName("cca2") val cca2: String,
    @SerializedName("currencies") val currencies: Map<String, Currency>,
    @SerializedName("idd") val idd: Idd,
    @SerializedName("capital") val capital: List<String>,
    @SerializedName("region") val region: String,
    @SerializedName("subregion") val subregion: String,
    @SerializedName("languages") val languages: Map<String, String>,
    @SerializedName("population") val population: Int
)

data class Flags(
    @SerializedName("png") val png: String,
    @SerializedName("svg") val svg: String,
    @SerializedName("alt") val alt: String
)

data class Name(
    @SerializedName("common") val common: String,
    @SerializedName("official") val official: String,
    @SerializedName("nativeName") val nativeName: Map<String, NativeName>
)

data class NativeName(
    @SerializedName("official") val official: String,
    @SerializedName("common") val common: String
)

data class Currency(
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String
)

data class Idd(
    @SerializedName("root") val root: String,
    @SerializedName("suffixes") val suffixes: List<String>
)