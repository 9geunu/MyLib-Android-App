package com.gunu.mylib.data.repository

import com.gunu.mylib.data.local.BookDao
import com.gunu.mylib.data.mapper.Mapper
import com.gunu.mylib.data.remote.BookApi
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentLinkedQueue

class Repository(
        private val dao: BookDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): IRepository {

    override fun observeBookmarkedBooks(): Flow<List<Book>> {
        return dao.observeBookmarkedBooks().map{ bookList ->
            bookList.map(Mapper::dataToDomain)
        }
    }

    @Throws(Exception::class)
    override suspend fun getBooks() = withContext(ioDispatcher) {
        BookApi.retrofitService.getBooks().books.map(Mapper::dataToDomain)
    }

    override suspend fun getMemo(isbn13: Long) = withContext(ioDispatcher) {
        dao.getMemo(isbn13)
    }

    @Throws(Exception::class)
    override suspend fun searchBooks(query: String) = withContext(ioDispatcher) {
        val bookList = ConcurrentLinkedQueue<Book>()

        val firstResponse = BookApi.retrofitService.searchBook(query, 1)
        bookList.addAll(firstResponse.books.map(Mapper::dataToDomain))

        coroutineScope {
            (2..firstResponse.getLastPage()).forEach { i ->
                launch {
                    bookList.addAll(BookApi.retrofitService.searchBook(query, i).books.map(Mapper::dataToDomain))
                }
            }
        }

        return@withContext bookList.toList()
    }

    @Throws(Exception::class)
    override suspend fun getDetailBook(isbn: String) = withContext(ioDispatcher) {
        BookApi.retrofitService.getDetailBook(isbn)
    }

    override suspend fun getBookByIsbn(isbn: Long) = withContext(ioDispatcher) {
        dao.getBookByIsbn(isbn)?.let(Mapper::dataToDomain)
    }

    override suspend fun insertBook(book: Book) = withContext(ioDispatcher) {
        dao.insertBook(Mapper.domainToData(book))
    }

    override suspend fun updateBookmark(book: Book, isBookmarked: Boolean) = withContext(ioDispatcher) {
        val storedBook = dao.getBookByIsbn(book.isbn13)

        if (storedBook == null) {
            book.isBookmarked = isBookmarked
            dao.insertBook(Mapper.domainToData(book))
        } else {
            dao.updateBookmark(book.isbn13, isBookmarked)
        }
    }

    override suspend fun updateMemo(isbn13: Long, memo: String) = withContext(ioDispatcher) {
        dao.updateMemo(isbn13, memo)
    }

    override suspend fun deleteAllBooks() = withContext(ioDispatcher) {
        dao.deleteAllBooks()
    }

    override suspend fun deleteBookmarkedBooks() = withContext(ioDispatcher) {
        dao.deleteBookmarkedBooks()
    }

    override suspend fun deleteBook(isbn: Long) = withContext(ioDispatcher) {
        dao.deleteBook(isbn)
    }
}