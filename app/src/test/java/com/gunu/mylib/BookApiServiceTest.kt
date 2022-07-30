package com.gunu.mylib

import com.gunu.mylib.data.remote.BookApi
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

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

    @Test
    fun testGetDetailBooks() {
        runBlocking {
            try {
                val books = BookApi.retrofitService.getDetailBook(9780134685991.toString())

                assertEquals("Effective Java, 3rd Edition", books.title)
                assertEquals("", books.subtitle)
                assertEquals("Joshua Bloch", books.authors)
                assertEquals("Addison-Wesley", books.publisher)
                assertEquals("English", books.language)
                assertEquals("0134685997", books.isbn10)
                assertEquals("9780134685991", books.isbn13)
                assertEquals("416", books.pages)
                assertEquals("2017", books.year)
                assertEquals("4", books.rating)
                assertTrue(books.desc.contains("Java has changed dramatically since the previous edition of Effective Java"))
                assertEquals("$38.00", books.price)
                assertEquals("https://itbook.store/img/books/9780134685991.png", books.image)
                assertEquals("https://itbook.store/books/9780134685991", books.url)
            } catch (e: Exception) {
                fail()
            }
        }
    }
}