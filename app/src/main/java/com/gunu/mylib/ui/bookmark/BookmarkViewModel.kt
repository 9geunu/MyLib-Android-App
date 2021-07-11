package com.gunu.mylib.ui.bookmark

import androidx.lifecycle.*
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.BookOpenViewModel
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class BookmarkViewModel(private val repository: IRepository) : ViewModel(), BookOpenViewModel {

    private var _items: LiveData<List<Book>> = repository.observeBookmarkedBooks().asLiveData(viewModelScope.coroutineContext)

    private val _openUrlEvent = MutableLiveData<Event<String>>()
    val openUrlEvent: LiveData<Event<String>> = _openUrlEvent

    private val _openDetailBookEvent = MutableLiveData<Event<Book>>()
    val openDetailBookEvent = _openDetailBookEvent

    private val _dataLoading = MutableLiveData<Boolean>()

    private val _isError = MutableLiveData<Boolean>()

    private val _isBookListEmpty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    override fun getBookList(): LiveData<List<Book>> {
        return _items
    }

    override fun isLoading(): LiveData<Boolean> {
        return _dataLoading
    }

    override fun openDetailBook(book: Book) {
        viewModelScope.launch {
            if (repository.getBookByIsbn(book.isbn13) == null) {
                repository.insertBook(book)
            }
            _openDetailBookEvent.postValue(Event(book))
        }
    }

    override fun openUrl(url: String) {
        _openUrlEvent.postValue(Event(url))
    }

    override fun refresh() {
        //Not Implement
    }

    override fun updateBookmark(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmark(book, isBookmarked)
        }
    }

    override fun isError(): LiveData<Boolean> {
        return _isError
    }

    override fun isBookListEmpty(): LiveData<Boolean> {
        return _isBookListEmpty
    }
}