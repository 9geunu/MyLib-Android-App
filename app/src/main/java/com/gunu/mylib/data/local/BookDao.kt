package com.gunu.mylib.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gunu.mylib.domain.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM book WHERE isBookmarked = 1")
    fun observeBookmarkedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book")
    suspend fun getBooks(): List<Book>

    @Query("SELECT * FROM book WHERE isbn13 = :isbn13")
    suspend fun getBookByIsbn(isbn13: Long): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("UPDATE book SET isBookmarked = :isBookmarked WHERE isbn13 = :isbn13")
    suspend fun updateBookmark(isbn13: Long, isBookmarked: Boolean)

    @Query("UPDATE book SET memo = :memo WHERE isbn13 = :isbn13")
    suspend fun updateMemo(isbn13: Long, memo: String)

    @Query("DELETE FROM book")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM book WHERE isBookmarked = 1")
    suspend fun deleteBookmarkedBooks()

    @Query("DELETE FROM book WHERE isbn13 = :isbn13")
    suspend fun deleteBook(isbn13: Long)
}