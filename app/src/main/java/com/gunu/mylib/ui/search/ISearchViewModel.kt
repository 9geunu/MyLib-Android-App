package com.gunu.mylib.ui.search

import androidx.lifecycle.MutableLiveData
import com.gunu.mylib.ui.BookOpenViewModel

interface ISearchViewModel: BookOpenViewModel {

    fun searchBooks(): (String) -> Unit

    fun getQuery(): MutableLiveData<String>
}