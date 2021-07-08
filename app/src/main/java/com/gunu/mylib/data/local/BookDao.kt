package com.gunu.mylib.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gunu.mylib.domain.Book

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun observeBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE isBookmarked = 1")
    fun observeBookmarkedBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM book")
    suspend fun getBooks(): List<Book>

    @Query("SELECT * FROM book WHERE id = :id")
    suspend fun getBookById(id: Long): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("UPDATE book SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    @Query("DELETE FROM book")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM book WHERE isBookmarked = 1")
    suspend fun deleteBookmarkedBooks()

    @Query("DELETE FROM book WHERE id = :id")
    suspend fun deleteBook(id: Long)
}