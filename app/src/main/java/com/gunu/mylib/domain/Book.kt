package com.gunu.mylib.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "book")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("isbn13")
    val isbn13: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
    val url: String,
    var isBookmarked: Boolean = false,
) {
}