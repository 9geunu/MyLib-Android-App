package com.gunu.mylib

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.data.local.BookLocalDatabase
import com.gunu.mylib.data.local.model.Book
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var database: BookLocalDatabase

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookLocalDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun testInsertTaskAndGetById() = runBlocking {

        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        val loaded = database.bookDao().getBookByIsbn(book.isbn13)

        Assert.assertNotNull(loaded)
        Assert.assertEquals(loaded?.title, book.title)
        Assert.assertEquals(loaded?.subtitle, book.subtitle)
        Assert.assertEquals(loaded?.isbn13, book.isbn13)
        Assert.assertEquals(loaded?.image, book.image)
        Assert.assertEquals(loaded?.url, book.url)
        Assert.assertEquals(loaded?.isBookmarked, book.isBookmarked)
    }

    @Test
    fun testInsertTaskReplacesOnConflict() = runBlocking {

        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        val newBook = Book(
            title = "newtitle",
            subtitle = "newsubtitle",
            isbn13 = book.isbn13,
            price = "newprice",
            image = "newimage",
            url = "newurl",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        database.bookDao().insertBook(newBook)

        // WHEN - Get the task by id from the database
        val loaded = database.bookDao().getBookByIsbn(book.isbn13)

        // THEN - The loaded data contains the expected values
        Assert.assertNotNull(loaded)
        Assert.assertEquals(loaded?.title, newBook.title)
        Assert.assertEquals(loaded?.subtitle, newBook.subtitle)
        Assert.assertEquals(loaded?.isbn13, newBook.isbn13)
        Assert.assertEquals(loaded?.image, newBook.image)
        Assert.assertEquals(loaded?.url, newBook.url)
        Assert.assertEquals(loaded?.isBookmarked, newBook.isBookmarked)
    }

    @Test
    fun testInsertTaskAndGetTasks() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        val loaded = database.bookDao().getBooks()
        val first = loaded.first()

        Assert.assertEquals(loaded.size, 1)
        Assert.assertEquals(first.title, book.title)
        Assert.assertEquals(first.subtitle, book.subtitle)
        Assert.assertEquals(first.isbn13, book.isbn13)
        Assert.assertEquals(first.image, book.image)
        Assert.assertEquals(first.url, book.url)
        Assert.assertEquals(first.isBookmarked, book.isBookmarked)
    }

    @Test
    fun testUpdateBookmark() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        database.bookDao().updateBookmark(book.isbn13, true)

        val loaded = database.bookDao().getBookByIsbn(book.isbn13)

        Assert.assertEquals(loaded?.title, book.title)
        Assert.assertEquals(loaded?.subtitle, book.subtitle)
        Assert.assertEquals(loaded?.isbn13, book.isbn13)
        Assert.assertEquals(loaded?.image, book.image)
        Assert.assertEquals(loaded?.url, book.url)
        Assert.assertEquals(loaded?.isBookmarked, true)
    }

    @Test
    fun testDeleteById() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        database.bookDao().deleteBook(book.isbn13)

        val loaded = database.bookDao().getBookByIsbn(book.isbn13)

        Assert.assertNull(loaded)
    }

    @Test
    fun testDeleteAll() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        val newBook = Book(
            title = "newtitle",
            subtitle = "newsubtitle",
            isbn13 = book.isbn13,
            price = "newprice",
            image = "newimage",
            url = "newurl",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        database.bookDao().insertBook(newBook)

        database.bookDao().deleteAllBooks()

        val loaded = database.bookDao().getBooks()

        Assert.assertEquals(0, loaded.size)
    }

    @Test
    fun testDeleteBookmarked() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        val newBook = Book(
            title = "newtitle",
            subtitle = "newsubtitle",
            isbn13 = 2,
            price = "newprice",
            image = "newimage",
            url = "newurl",
            isBookmarked = true
        )

        database.bookDao().insertBook(book)

        database.bookDao().insertBook(newBook)

        database.bookDao().deleteBookmarkedBooks()

        val loaded = database.bookDao().getBooks()
        val first = loaded.first()
        Assert.assertEquals(1, loaded.size)
        Assert.assertNull(database.bookDao().getBookByIsbn(2))
        Assert.assertEquals(first.title, book.title)
        Assert.assertEquals(first.subtitle, book.subtitle)
        Assert.assertEquals(first.isbn13, book.isbn13)
        Assert.assertEquals(first.image, book.image)
        Assert.assertEquals(first.url, book.url)
        Assert.assertEquals(first.isBookmarked, book.isBookmarked)
    }

    @Test
    fun testUpdateMemo() = runBlocking {
        val book = Book(
            title = "title",
            subtitle = "subtitle",
            isbn13 = 1,
            price = "price",
            image = "image",
            url = "url",
            isBookmarked = false
        )

        database.bookDao().insertBook(book)

        database.bookDao().updateMemo(book.isbn13, "Memo")

        val loaded = database.bookDao().getBookByIsbn(book.isbn13)

        Assert.assertEquals("Memo", loaded?.memo)
    }
}