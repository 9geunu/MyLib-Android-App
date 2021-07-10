package com.gunu.mylib

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gunu.mylib.data.remote.BookApi
import com.gunu.mylib.domain.Book
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class BookApiServiceTest {

    @Test
    fun testGetNewBooks() {
        runBlocking {
            try {
                val books = BookApi.retrofitService.getBooks().books
                assertNotNull(books)

                books.forEach {
                    assertNotNull(it.title)
                    assertNotNull(it.subtitle)
                    assertNotSame(0, it.isbn13)
                    assertNotNull(it.isbn13)
                    assertNotNull(it.price)
                    assertNotNull(it.image)
                    assertNotNull(it.isBookmarked)
                    assertFalse(it.isBookmarked)
                }
            } catch (e: Exception) {
                Assert.fail()
            }
        }
    }

    @Test
    fun testGetSearchBooks() {
        runBlocking {
            try {
                val books = BookApi.retrofitService.searchBook("java", 1).books
                assertNotNull(books)

                books.forEach {
                    assertNotNull(it.title)
                    assertNotNull(it.subtitle)
                    assertNotSame(0, it.isbn13)
                    assertNotNull(it.isbn13)
                    assertNotNull(it.price)
                    assertNotNull(it.image)
                    assertNotNull(it.isBookmarked)
                    assertFalse(it.isBookmarked)
                }
            } catch (e: Exception) {
                fail()
            }
        }
    }
}