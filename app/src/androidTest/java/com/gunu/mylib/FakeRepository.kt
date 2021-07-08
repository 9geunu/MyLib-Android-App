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
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.BookResponse
import com.gunu.mylib.domain.IRepository
import java.util.*

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeRepository : IRepository {

    var BookServiceData: LinkedHashMap<Long, Book> = LinkedHashMap()

    private val observableBookList = MutableLiveData<List<Book>>()

    override fun observeBooks(): LiveData<List<Book>> {
        observableBookList.value = BookServiceData.values.toList()

        return observableBookList
    }

    override fun observeBookmarkedBooks():  LiveData<List<Book>> {
        observableBookList.value = BookServiceData.values.toList().filter { it.isBookmarked }

        return observableBookList
    }

    override suspend fun getBooks(): BookResponse {
        return BookResponse(BookServiceData.values.toList())
    }

    override suspend fun getBookById(BookId: Long): Book? {
        return BookServiceData[BookId]
    }

    override suspend fun insertBook(Book: Book) {
        BookServiceData[Book.id] = Book
    }

    override suspend fun updateBookmark(id: Long, isBookmarked: Boolean) {
        BookServiceData[id]?.isBookmarked = isBookmarked
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

    override suspend fun deleteBook(BookId: Long) {
        BookServiceData.remove(BookId)

        observableBookList.value = BookServiceData.values.toList()
    }
}