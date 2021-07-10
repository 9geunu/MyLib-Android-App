package com.gunu.mylib.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModel(private val repository: IRepository): ViewModel() {

    private val _book = MutableLiveData<Book>()
    val book = MutableLiveData<Book>()

    suspend fun start(isbn: Long) {
        repository.getBookByIsbn(isbn)?.let {
            loadBook(it)
        }
    }

    fun loadBook(book: Book) {
        this.book.postValue(book)
    }

    val updateBookmark: (Book, Boolean) -> Unit = fun(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmark(book, isBookmarked)
        }
    }
}