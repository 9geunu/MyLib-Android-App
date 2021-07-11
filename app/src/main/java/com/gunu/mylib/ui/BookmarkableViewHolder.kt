package com.gunu.mylib.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunu.mylib.databinding.ItemBookmarkableBookBinding
import com.gunu.mylib.domain.Book

class BookmarkableViewHolder private constructor(val binding: ItemBookmarkableBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: BookOpenViewModel, item: Book) {

        binding.viewModel = viewModel
        binding.book = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): BookmarkableViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookmarkableBookBinding.inflate(layoutInflater, parent, false)

            return BookmarkableViewHolder(binding)
        }
    }
}