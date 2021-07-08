package com.gunu.mylib.data.remote

import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.BookResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.itbook.store/1.0/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BookApiService {
    @GET("new")
    suspend fun getBooks(): BookResponse

    @GET("search/{query}")
    suspend fun searchBook(@Path("query") query: String): BookResponse
}

object BookApi {
    val retrofitService : BookApiService by lazy {
        retrofit.create(BookApiService::class.java) }
}