package com.gunu.mylib.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "book")
data class Book(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @PrimaryKey
    @SerializedName("isbn13")
    val isbn13: Long,
    @SerializedName("price")
    val price: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
    val url: String,
    var isBookmarked: Boolean = false,
    var memo: String? = ""
) {
    fun getIsbnString(): String = "isbn13:$isbn13"

    fun getPriceString(): String = if (price.equals("$0.00")) "FREE" else price
}