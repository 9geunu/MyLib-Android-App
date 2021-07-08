package com.gunu.mylib.domain

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface IRepository {

    fun observeBooks(): LiveData<List<Book>>

    fun observeBookmarkedBooks(): LiveData<List<Book>>

    suspend fun getBooks(): BookResponse

    suspend fun getBookById(id: Long): Book?

    suspend fun insertBook(book: Book)

    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    suspend fun deleteAllBooks()

    suspend fun deleteBookmarkedBooks()

    suspend fun deleteBook(id: Long)
}