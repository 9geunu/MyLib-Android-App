package com.gunu.mylib.ui.newbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gunu.mylib.data.Repository
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.BookOpenViewModel
import com.gunu.mylib.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class NewBookViewModel(private val repository: IRepository) : ViewModel(), BookOpenViewModel {

    private var _items = MutableLiveData<List<Book>>()

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    private val _openUrlEvent = MutableLiveData<Event<String>>()
    val openUrlEvent: LiveData<Event<String>> = _openUrlEvent

    private val _dataLoading = MutableLiveData<Boolean>()

    fun start() {
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                val bookResponse = repository.getBooks()

                _items.value = bookResponse.books.also {
                    it.forEach { book -> repository.insertBook(book) }
                }
                _dataLoading.value = false
            } catch (e: Exception) {
                _toastText.value = Event("Error occured in getting books!")
                e.printStackTrace()
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
        TODO("Not yet implemented")
    }

    override fun openUrl(url: String) {
        _openUrlEvent.value = Event(url)
    }
}