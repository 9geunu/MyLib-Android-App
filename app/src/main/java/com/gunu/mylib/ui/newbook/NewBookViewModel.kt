package com.gunu.mylib.ui.newbook

import androidx.lifecycle.*
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository
import com.gunu.mylib.domain.usecase.GetBookByIsbnUseCase
import com.gunu.mylib.domain.usecase.GetBooksUseCase
import com.gunu.mylib.domain.usecase.InsertBookUseCase
import com.gunu.mylib.domain.usecase.UpdateBookmarkUseCase
import com.gunu.mylib.ui.BookOpenViewModel
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.launch

class NewBookViewModel(private val repository: IRepository) : ViewModel(), BookOpenViewModel {

    private var _items = MutableLiveData<List<Book>>()

    private val _openUrlEvent = MutableLiveData<Event<String>>()
    val openUrlEvent: LiveData<Event<String>> = _openUrlEvent

    private val _openDetailBookEvent = MutableLiveData<Event<Book>>()
    val openDetailBookEvent = _openDetailBookEvent

    private val _dataLoading = MutableLiveData<Boolean>()

    private val _isError = MutableLiveData<Boolean>()

    private val _isBookListEmpty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun start() {
        _dataLoading.value = true
        refresh()
    }

    override fun getBookList(): LiveData<List<Book>> {
        return _items
    }

    override fun isLoading(): LiveData<Boolean> {
        return _dataLoading
    }

    override fun openDetailBook(book: Book) {
        viewModelScope.launch {
            val bookByIsbn = GetBookByIsbnUseCase(repository, book.isbn13).execute()

            if (bookByIsbn == null) {
                InsertBookUseCase(repository, book).execute()
            }

            _openDetailBookEvent.postValue(Event(book))
        }
    }

    override fun openUrl(url: String) {
        _openUrlEvent.postValue(Event(url))
    }

    override fun refresh() {
        _isError.value = false
        viewModelScope.launch {
            try {
                val bookResponse = GetBooksUseCase(repository).execute()
                _items.value = bookResponse
            } catch (e: Exception) {
                _isError.value = true
                _items.value = listOf()
                e.printStackTrace()
            } finally {
                _dataLoading.value = false
            }
        }
    }

    override fun updateBookmark(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            UpdateBookmarkUseCase(repository, book, isBookmarked).execute()
        }
    }

    override fun isError(): LiveData<Boolean> {
        return _isError
    }

    override fun isBookListEmpty(): LiveData<Boolean> {
        return _isBookListEmpty
    }
}