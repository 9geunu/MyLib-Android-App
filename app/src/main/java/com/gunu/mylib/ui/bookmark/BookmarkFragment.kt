package com.gunu.mylib.ui.bookmark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.gunu.mylib.databinding.FragmentBookmarkBinding
import com.gunu.mylib.domain.Book
import com.gunu.mylib.ui.BookAdapter
import com.gunu.mylib.ui.BookmarkableBookAdapter
import com.gunu.mylib.ui.EventObserver
import com.gunu.mylib.ui.newbook.NewBookFragmentDirections
import com.gunu.mylib.ui.search.SearchViewModel
import com.gunu.mylib.util.getViewModelFactory

class BookmarkFragment : Fragment() {

    private val bookmarkViewModel: BookmarkViewModel by viewModels<BookmarkViewModel> { getViewModelFactory() }

    private lateinit var viewBinding: FragmentBookmarkBinding

    private lateinit var listAdapter: BookmarkableBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentBookmarkBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@BookmarkFragment.viewLifecycleOwner
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.viewModel = bookmarkViewModel
        viewBinding.booklistLayout.viewModel = bookmarkViewModel
        setupListAdapter()
        setupOpenUrlAction()
        setupNavigation()
    }

    private fun setupListAdapter() {
        listAdapter = BookmarkableBookAdapter(bookmarkViewModel)
        viewBinding.booklistLayout.bookRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.booklistLayout.bookRecyclerView.adapter = listAdapter
    }

    private fun setupOpenUrlAction() {
        bookmarkViewModel.openUrlEvent.observe(viewLifecycleOwner, EventObserver {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))

            requireContext().startActivity(intent)
        })
    }

    private fun setupNavigation() {
        bookmarkViewModel.openDetailBookEvent.observe(viewLifecycleOwner, EventObserver {
            openDetalBook(it)
        })
    }

    private fun openDetalBook(book: Book) {
        val action = BookmarkFragmentDirections.actionNavigationBookmarkToDetailBookActivity(book.isbn13)
        findNavController().navigate(action)
    }
}