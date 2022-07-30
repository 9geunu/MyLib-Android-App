package com.gunu.mylib.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gunu.mylib.domain.model.Book

class BookAdapter(private val bookOpenViewModel: BookOpenViewModel):
    ListAdapter<Book, BookViewHolder>(BookDiffCallback()) {

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(bookOpenViewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder.from(parent)
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.isbn13 == newItem.isbn13
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}