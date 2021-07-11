package com.gunu.mylib.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunu.mylib.databinding.ItemBookBinding
import com.gunu.mylib.domain.Book

class BookViewHolder private constructor(val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: BookOpenViewModel, item: Book) {

        binding.viewModel = viewModel
        binding.book = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): BookViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)

            return BookViewHolder(binding)
        }
    }
}