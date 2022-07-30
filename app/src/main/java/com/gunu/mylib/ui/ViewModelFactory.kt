package com.gunu.mylib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gunu.mylib.domain.repository.IRepository
import com.gunu.mylib.ui.bookmark.BookmarkViewModel
import com.gunu.mylib.ui.detail.DetailViewModel
import com.gunu.mylib.ui.newbook.NewBookViewModel
import com.gunu.mylib.ui.search.SearchViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: IRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(NewBookViewModel::class.java) ->
                NewBookViewModel(repository)
            isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(repository)
            isAssignableFrom(BookmarkViewModel::class.java) ->
                BookmarkViewModel(repository)
            isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(repository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}