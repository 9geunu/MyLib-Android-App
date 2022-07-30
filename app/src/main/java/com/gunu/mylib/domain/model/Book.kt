package com.gunu.mylib.domain.model

data class Book(
    val title: String,
    val subtitle: String,
    val isbn13: Long,
    val price: String,
    val image: String,
    val url: String,
    var isBookmarked: Boolean = false,
    var memo: String? = ""
) {
    fun getIsbnString(): String = "isbn13:$isbn13"

    fun getPriceString(): String = if (price.equals("$0.00")) "FREE" else price
}