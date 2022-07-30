package com.gunu.mylib.data.remote.model

import com.google.gson.annotations.SerializedName
import com.gunu.mylib.data.local.model.Book

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
