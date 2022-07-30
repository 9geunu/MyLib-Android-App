package com.gunu.mylib

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gunu.mylib.data.local.model.Book
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookTest {

    @Test
    fun testParseBook() {
        val example = "{" +
                "\"title\":\"TypeScript Notes for Professionals\"," +
                "\"subtitle\":\"\"," +
                "\"isbn13\":\"1001622115721\"," +
                "\"price\":\"\$0.00\"," +
                "\"image\":\"https://itbook.store/img/books/1001622115721.png\"," +
                "\"url\":\"https://itbook.store/books/1001622115721\"" +
                "}"

        val book = Gson().fromJson<Book>(example, object : TypeToken<Book>(){}.type)

        println(book)

        Assert.assertEquals("TypeScript Notes for Professionals", book.title)
        Assert.assertEquals("", book.subtitle)
        Assert.assertEquals(1001622115721, book.isbn13)
        Assert.assertEquals("\$0.00", book.price)
        Assert.assertEquals("https://itbook.store/img/books/1001622115721.png", book.image)
        Assert.assertEquals("https://itbook.store/books/1001622115721", book.url)
        Assert.assertFalse(book.isBookmarked)
    }
}