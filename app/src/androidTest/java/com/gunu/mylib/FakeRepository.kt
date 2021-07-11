/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gunu.mylib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeRepository : IRepository {

    var BookServiceData: LinkedHashMap<Long, Book> = LinkedHashMap()

    private val observableBookList = MutableLiveData<List<Book>>()

    override fun observeBookmarkedBooks(): Flow<List<Book>> {
        observableBookList.postValue(BookServiceData.values.toList().filter { it.isBookmarked })

        return observableBookList.asFlow()
    }

    override suspend fun getBooks(): List<Book> {
        return BookServiceData.values.toList()
    }

    override suspend fun searchBooks(query: String): List<Book> {
        return BookServiceData.values.filter { it.title.contains(query) }
    }

    override suspend fun getBookByIsbn(isbn: Long): Book? {
        return BookServiceData[isbn]
    }

    override suspend fun insertBook(book: Book) {
        BookServiceData[book.isbn13] = book
    }

    override suspend fun updateBookmark(book: Book, isBookmarked: Boolean) {
        BookServiceData[book.isbn13]?.isBookmarked = isBookmarked
    }

    override suspend fun deleteBookmarkedBooks() {
        BookServiceData = BookServiceData.filterValues {
            !it.isBookmarked
        } as LinkedHashMap<Long, Book>
    }

    override suspend fun deleteAllBooks() {
        BookServiceData.clear()

        observableBookList.value = BookServiceData.values.toList()
    }

    override suspend fun deleteBook(isbn: Long) {
        BookServiceData.remove(isbn)

        observableBookList.postValue(BookServiceData.values.toList())
    }
}
