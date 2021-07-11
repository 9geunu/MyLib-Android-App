package com.gunu.mylib.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModel(private val repository: IRepository): ViewModel() {

    val book = MutableLiveData<Book>()

    val memo = MutableLiveData<String>()

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText


    suspend fun start(isbn: Long) {
        repository.getBookByIsbn(isbn)?.let {
            loadBook(it)
        }
    }

    fun loadBook(book: Book) {
        this.book.postValue(book)
        this.memo.postValue(book.memo)
    }

    val updateBookmark: (Book, Boolean) -> Unit = fun(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmark(book, isBookmarked)
        }
    }

    fun saveMemo() {
        val currentBook = book.value
        val currentMemo = memo.value

        if (currentBook != null && currentMemo != null) {
            viewModelScope.launch {
                repository.updateMemo(currentBook.isbn13, currentMemo)
                _toastText.value = Event("메모가 저장되었습니다.")
            }
        }
    }
}