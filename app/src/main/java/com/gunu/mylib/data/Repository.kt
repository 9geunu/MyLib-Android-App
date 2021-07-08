package com.gunu.mylib.data

import androidx.lifecycle.LiveData
import com.gunu.mylib.data.local.BookDao
import com.gunu.mylib.data.remote.BookApi
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
        private val dao: BookDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): IRepository {

    override fun observeBooks(): LiveData<List<Book>> {
        return dao.observeBooks()
    }

    override fun observeBookmarkedBooks(): LiveData<List<Book>> {
        return dao.observeBookmarkedBooks()
    }

    override suspend fun getBooks() = withContext(ioDispatcher) {
        BookApi.retrofitService.getBooks()
    }

    override suspend fun getBookById(id: Long) = withContext(ioDispatcher) {
        dao.getBookById(id)
    }

    override suspend fun insertBook(book: Book) = withContext(ioDispatcher) {
        dao.insertBook(book)
    }

    override suspend fun updateBookmark(id: Long, isBookmarked: Boolean) = withContext(ioDispatcher) {
        dao.updateBookmark(id, isBookmarked)
    }

    override suspend fun deleteAllBooks() = withContext(ioDispatcher) {
        dao.deleteAllBooks()
    }

    override suspend fun deleteBookmarkedBooks() = withContext(ioDispatcher) {
        dao.deleteBookmarkedBooks()
    }

    override suspend fun deleteBook(id: Long) = withContext(ioDispatcher) {
        dao.deleteBook(id)
    }
}