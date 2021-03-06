package com.gunu.mylib.ui

import androidx.lifecycle.LiveData
import com.gunu.mylib.domain.Book

interface BookOpenViewModel {

    fun getBookList(): LiveData<List<Book>>

    fun isLoading(): LiveData<Boolean>

    fun openDetailBook(book: Book)

    fun openUrl(url: String)

    fun refresh()

    fun updateBookmark(book: Book, isBookmarked: Boolean)

    fun isError(): LiveData<Boolean>

    fun isBookListEmpty(): LiveData<Boolean>
}