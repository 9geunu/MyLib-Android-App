package com.gunu.mylib.ui.detail

import androidx.lifecycle.*
import com.gunu.mylib.data.remote.BookApi
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.DetailBook
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.jvm.Throws

class DetailViewModel(private val repository: IRepository): ViewModel() {

    val book = MutableLiveData<Book>()

    val detailBook = MutableLiveData<DetailBook>()

    val memo = MutableLiveData<String>()

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    private val _openUrlEvent = MutableLiveData<Event<String>>()
    val openUrlEvent: LiveData<Event<String>> = _openUrlEvent

    private val _dataLoading = MutableLiveData<Boolean>()

    private val _isError = MutableLiveData<Boolean>()

    val isHideTextLayout = Transformations.switchMap(_dataLoading) { isLoading ->
        Transformations.map(_isError) { isError ->
            isLoading || isError
        }
    }

    fun start(isbn: Long) {
        _dataLoading.value = true

        refresh(isbn)
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

    fun refresh(isbn: Long) {
        _isError.value = false

        viewModelScope.launch {
            try {
                repository.getBookByIsbn(isbn)?.let {
                    this@DetailViewModel.book.value = it
                }

                repository.getMemo(isbn)?.let {
                    this@DetailViewModel.memo.value = it
                }
                this@DetailViewModel.detailBook.value = repository.getDetailBook(isbn.toString())

            } catch (e: Exception) {
                _isError.value = true
                e.printStackTrace()
            } finally {
                _dataLoading.value = false
            }
        }
    }

    fun openUrl(url: String) {
        _openUrlEvent.postValue(Event(url))
    }

    fun isLoading(): LiveData<Boolean> {
        return _dataLoading
    }

    fun isError(): LiveData<Boolean> {
        return _isError
    }
}