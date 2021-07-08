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
import com.gunu.mylib.R
import com.gunu.mylib.databinding.FragmentNewbookBinding
import com.gunu.mylib.domain.Book
import com.gunu.mylib.ui.BookAdapter
import com.gunu.mylib.ui.BookOpenViewModel
import com.gunu.mylib.ui.EventObserver
import com.gunu.mylib.util.getViewModelFactory

class NewBookFragment : Fragment() {

    private val newBookViewModel: NewBookViewModel by viewModels { getViewModelFactory() }

    private lateinit var viewBinding: FragmentNewbookBinding

    private lateinit var listAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewbookBinding.inflate(inflater, container, false).apply {
            booklistLayout.viewModel = newBookViewModel
            lifecycleOwner = this@NewBookFragment.viewLifecycleOwner
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        setupToast()
        newBookViewModel.start()
    }

    private fun setupListAdapter() {
        val viewModel = viewBinding.booklistLayout.viewModel
        if (viewModel != null) {
            listAdapter = BookAdapter(viewModel)
            viewBinding.booklistLayout.bookRecyclerView.adapter = listAdapter
        } else {
            Log.e("MyLib", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupToast() {
        newBookViewModel.toastText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupOpenUrlAction() {
        newBookViewModel.openUrlEvent.observe(viewLifecycleOwner, EventObserver {
            Log.e("MyLib", "Url : $it")

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))

            requireContext().startActivity(intent)
        })
    }
}