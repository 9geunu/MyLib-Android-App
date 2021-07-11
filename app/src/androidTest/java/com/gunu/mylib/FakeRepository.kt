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

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.DetailBook
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

    @Throws(Exception::class)
    override suspend fun getBooks(): List<Book> {
        if (!isNetworkConnected()) throw Exception("Network disconnected")

        return BookServiceData.values.toList()
    }

    @Throws(Exception::class)
    override suspend fun searchBooks(query: String): List<Book> {
        if (!isNetworkConnected()) throw Exception("Network disconnected")

        return BookServiceData.values.filter { it.title.contains(query) }
    }

    override suspend fun getDetailBook(isbn: String): DetailBook {
        val detailBook = "{\"error\":\"0\",\"title\":\"Effective Java, 3rd Edition\"," +
                "\"subtitle\":\"\",\"authors\":\"Joshua Bloch\",\"publisher\":\"Addison-Wesley\"," +
                "\"language\":\"English\",\"isbn10\":\"0134685997\",\"isbn13\":\"9780134685991\"," +
                "\"pages\":\"416\",\"year\":\"2017\",\"rating\":\"4\"," +
                "\"desc\":\"Java has changed dramatically since the previous edition of Effective Java " +
                "was published shortly after the release of Java 6. This Jolt award-winning classic has now been " +
                "thoroughly updated to take full advantage of the latest language and library features." +
                " The support in modern Java for multiple pa...\"," +
                "\"price\":\"\$38.00\",\"image\":\"https://itbook.store/img/books/9780134685991.png\"," +
                "\"url\":\"https://itbook.store/books/9780134685991\"}"

        return Gson().fromJson<DetailBook>(detailBook, object : TypeToken<DetailBook>(){}.type)
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

    override suspend fun updateMemo(isbn13: Long, memo: String) {
        BookServiceData[isbn13]?.memo = memo
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

    fun isNetworkConnected(): Boolean {
        val context = InstrumentationRegistry.getInstrumentation().context
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false

            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks: Array<Network> = cm.allNetworks

            for (n in networks) {
                val nInfo: NetworkInfo? = cm.getNetworkInfo(n)
                if (nInfo != null && nInfo.isConnected) return true
            }
        } else {
            val networks = cm.allNetworkInfo
            for (nInfo in networks) {
                if (nInfo != null && nInfo.isConnected) return true
            }
        }

        return false
    }
}
