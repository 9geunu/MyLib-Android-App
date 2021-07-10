package com.gunu.mylib.util

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.gunu.mylib.domain.Book
import com.gunu.mylib.ui.BookAdapter


@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Book>?) {
    items?.let {
        (listView.adapter as BookAdapter).submitList(items)
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
fun setOnExtendedFloatingActionButtonCheck(efab: ExtendedFloatingActionButton, book: Book?, onCheck: (Book, Boolean) -> Unit) {
    efab.shrink()

    book?.let {
        efab.isChecked = it.isBookmarked
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