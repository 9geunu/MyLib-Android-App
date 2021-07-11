package com.gunu.mylib.ui.search

import androidx.lifecycle.*
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: IRepository) : ViewModel(), ISearchViewModel {

    private var _items = MutableLiveData<List<Book>>()

    private val _openUrlEvent = MutableLiveData<Event<String>>()
    val openUrlEvent: LiveData<Event<String>> = _openUrlEvent

    private val _openDetailBookEvent = MutableLiveData<Event<Book>>()
    val openDetailBookEvent = _openDetailBookEvent

    private val _dataLoading = MutableLiveData<Boolean>()

    private val _isError = MutableLiveData<Boolean>()

    private val _isBookListEmpty: LiveData<Boolean> = Transformations.switchMap(_items) { list ->
        Transformations.map(_isError) { error ->
            if (error) {
                false
            } else {
                list.isEmpty()
            }
        }
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
        this.query.value?.let {
            searchBooks()(it)
        }
    }

    override fun updateBookmark(book: Book, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmark(book, isBookmarked)
        }
    }

    private var query = MutableLiveData<String>("")

    override fun searchBooks() = fun(query: String) {
        _isError.value = false
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                val bookResponse = repository.searchBooks(query)
                _items.value = bookResponse
            } catch (e: Exception) {
                _isError.value = true
                e.printStackTrace()
            } finally {
                _dataLoading.value = false
            }
        }
    }

    override fun getQuery(): MutableLiveData<String> {
        return query
    }

    override fun isError(): LiveData<Boolean> {
        return _isError
    }

    override fun isBookListEmpty(): LiveData<Boolean> {
        return _isBookListEmpty
    }
}