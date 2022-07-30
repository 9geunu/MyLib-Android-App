package com.gunu.mylib.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.gunu.mylib.databinding.FragmentSearchBinding
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.ui.BookAdapter
import com.gunu.mylib.ui.EventObserver
import com.gunu.mylib.util.getViewModelFactory

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels<SearchViewModel> { getViewModelFactory() }

    private lateinit var viewBinding: FragmentSearchBinding

    private lateinit var listAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {

            lifecycleOwner = this@SearchFragment.viewLifecycleOwner
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.viewModel = searchViewModel
        viewBinding.booklistLayout.viewModel = searchViewModel
        setupListAdapter()
        setupOpenUrlAction()
        setupNavigation()
    }

    private fun setupListAdapter() {
        listAdapter = BookAdapter(searchViewModel)
        viewBinding.booklistLayout.bookRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.booklistLayout.bookRecyclerView.adapter = listAdapter
    }


    private fun setupOpenUrlAction() {
        searchViewModel.openUrlEvent.observe(viewLifecycleOwner, EventObserver {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))

            requireContext().startActivity(intent)
        })
    }

    private fun setupNavigation() {
        searchViewModel.openDetailBookEvent.observe(viewLifecycleOwner, EventObserver {
            openDetalBook(it)
        })
    }

    private fun openDetalBook(book: Book) {
        val action = SearchFragmentDirections.actionNavigationSearchToDetailBookActivity(book.isbn13)
        findNavController().navigate(action)
    }
}