package com.gunu.mylib.domain

import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun observeBookmarkedBooks(): Flow<List<Book>>

    suspend fun getBooks(): List<Book>

    suspend fun searchBooks(query: String): List<Book>

    suspend fun getBookByIsbn(isbn: Long): Book?

    suspend fun insertBook(book: Book)

    suspend fun updateBookmark(book: Book, isBookmarked: Boolean)

    suspend fun updateMemo(isbn13: Long, memo: String)

    suspend fun deleteAllBooks()

    suspend fun deleteBookmarkedBooks()

    suspend fun deleteBook(isbn: Long)
}