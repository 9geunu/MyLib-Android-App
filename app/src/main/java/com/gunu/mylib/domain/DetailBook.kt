package com.gunu.mylib.domain

import com.google.gson.annotations.SerializedName

data class DetailBook(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("authors")
    val authors: String,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("isbn10")
    val isbn10: String,
    @SerializedName("isbn13")
    val isbn13: String,
    @SerializedName("pages")
    val pages: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
    val url: String
) {
    fun getPriceString(): String = if (price.equals("$0.00")) "FREE" else price

    fun getRatingString(): String = "$rating/5"

}