package com.gunu.mylib.ui.newbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.gunu.mylib.R
import com.gunu.mylib.databinding.FragmentNewbookBinding
import com.gunu.mylib.domain.Book
import com.gunu.mylib.ui.BookAdapter
import com.gunu.mylib.ui.BookOpenViewModel
import com.gunu.mylib.ui.EventObserver
import com.gunu.mylib.util.getViewModelFactory

class NewBookFragment : Fragment() {

    private val newBookViewModel: NewBookViewModel by viewModels<NewBookViewModel> { getViewModelFactory() }

    private lateinit var viewBinding: FragmentNewbookBinding

    private lateinit var listAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewbookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@NewBookFragment.viewLifecycleOwner
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.viewModel = newBookViewModel
        viewBinding.booklistLayout.viewModel = newBookViewModel
        setupListAdapter()
        setupOpenUrlAction()
        setupNavigation()
        newBookViewModel.start()
    }

    private fun setupListAdapter() {
        listAdapter = BookAdapter(newBookViewModel)
        viewBinding.booklistLayout.bookRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.booklistLayout.bookRecyclerView.adapter = listAdapter
    }

    private fun setupOpenUrlAction() {
        newBookViewModel.openUrlEvent.observe(viewLifecycleOwner, EventObserver {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))

            requireContext().startActivity(intent)
        })
    }

    private fun setupNavigation() {
        newBookViewModel.openDetailBookEvent.observe(viewLifecycleOwner, EventObserver {
            openDetalBook(it)
        })
    }

    private fun openDetalBook(book: Book) {
        val action = NewBookFragmentDirections.actionNavigationNewbookToDetailBookActivity(book.isbn13)
        findNavController().navigate(action)
    }
}