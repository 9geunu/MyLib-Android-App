package com.gunu.mylib.domain

import com.google.gson.annotations.SerializedName

data class BookResponse(

    @SerializedName("total")
    val total: Long,
    @SerializedName("books")
    val books: List<Book>
){
    fun getLastPage(): Long {
        return if (total / 10 >= 100) 100 else total / 10
     }
}
