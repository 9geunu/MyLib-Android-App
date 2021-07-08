package com.gunu.mylib.domain

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("books")
    val books: List<Book>
)
