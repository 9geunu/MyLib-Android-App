package com.gunu.mylib.util

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.gunu.mylib.domain.Book
import com.gunu.mylib.ui.BookAdapter
import com.gunu.mylib.ui.BookmarkableBookAdapter


@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Book>?) {
    items?.let {
        (listView.adapter as BookAdapter).submitList(items)
    }
}

@BindingAdapter("app:bookmarkableItems")
fun setBookmarkableItems(listView: RecyclerView, items: List<Book>?) {
    items?.let {
        (listView.adapter as BookmarkableBookAdapter).submitList(items)
    }
}

@BindingAdapter("app:imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("app:book", "app:onCheckCanged")
fun setOnExtendedFloatingActionButtonCheck(
    efab: ExtendedFloatingActionButton,
    book: Book?,
    onCheck: (Book, Boolean) -> Unit
) {

    book?.let {
        efab.isChecked = it.isBookmarked
    }

    if (efab.isChecked) {
        efab.extend()
    } else {
        efab.shrink()
    }

    efab.addOnCheckedChangeListener { button, isChecked ->
        if (isChecked) {
            (button as ExtendedFloatingActionButton).extend()
        } else {
            (button as ExtendedFloatingActionButton).shrink()
        }

        book?.let {
            onCheck(it, isChecked)
        }
    }
}

@BindingAdapter("app:onQuery", "app:onChange")
fun setOnQueryTextListener(
    searchView: SearchView,
    search: (String) -> Unit,
    onChange: (String) -> Unit
) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                search(it)
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                onChange(it)
            }
            return false
        }
    })
}

@BindingAdapter("app:onSearch")
fun setOnEditorActionListener(editText: EditText, search: (String) -> Unit) {
    editText.setOnEditorActionListener(OnEditorActionListener { textView, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search(textView.text.toString())

            val imm =
                editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)

            return@OnEditorActionListener true
        }
        false
    })
}