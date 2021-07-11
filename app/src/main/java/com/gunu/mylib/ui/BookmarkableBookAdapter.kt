package com.gunu.mylib.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gunu.mylib.databinding.ItemBookBinding
import com.gunu.mylib.databinding.ItemBookmarkableBookBinding
import com.gunu.mylib.domain.Book

class BookmarkableBookAdapter(private val bookOpenViewModel: BookOpenViewModel):
    ListAdapter<Book, BookmarkableViewHolder>(BookDiffCallback()) {

    override fun onBindViewHolder(holder: BookmarkableViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(bookOpenViewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkableViewHolder {
        return BookmarkableViewHolder.from(parent)
    }
}