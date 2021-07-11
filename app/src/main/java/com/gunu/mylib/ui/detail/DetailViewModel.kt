package com.gunu.mylib.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunu.mylib.data.remote.BookApi
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.DetailBook
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    suspend fun start(isbn: Long) {
        loadBook(isbn)

        repository.getBookByIsbn(isbn)?.let {
            this@DetailViewModel.book.postValue(it)
            this@DetailViewModel.memo.postValue(it.memo)
        }
    }

    fun loadBook(isbn: Long) {
        _dataLoading.postValue(true)
        refresh(isbn)
    }

    val updateBookmark: (Book, Boolean) -> Unit = fun(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmark(book, isBookmarked)
        }
    }

    fun saveMemo() {
        val currentBook = detailBook.value
        val currentMemo = memo.value

        if (currentBook != null && currentMemo != null) {
            viewModelScope.launch {
                repository.updateMemo(currentBook.isbn13.toLong(), currentMemo)
                _toastText.value = Event("메모가 저장되었습니다.")
            }
        }
    }

    fun refresh(isbn: Long) {
        _isError.postValue(false)

        viewModelScope.launch {
            try {
                val response = BookApi.retrofitService.getDetailBook(isbn.toString())
                detailBook.postValue(response)

            } catch (e: Exception) {
                _toastText.postValue(Event("도서 정보를 불러오는 중에 오류가 발생했습니다."))
                _isError.postValue(true)
                e.printStackTrace()
            } finally {
                _dataLoading.postValue(false)
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