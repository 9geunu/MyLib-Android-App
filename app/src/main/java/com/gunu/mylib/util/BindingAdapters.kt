package com.gunu.mylib.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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