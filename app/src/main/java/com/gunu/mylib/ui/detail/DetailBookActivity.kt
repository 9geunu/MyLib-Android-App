package com.gunu.mylib.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.gunu.mylib.R
import com.gunu.mylib.databinding.ActivityDetailBookBinding
import com.gunu.mylib.util.getViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailBookActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by viewModels<DetailViewModel> { getViewModelFactory() }

    private val args: DetailBookActivityArgs by navArgs()

    private lateinit var viewBinding: ActivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.start(args.bookIsbn)

            withContext(Dispatchers.Main) {
                viewBinding = DataBindingUtil.setContentView(this@DetailBookActivity, R.layout.activity_detail_book)

                viewBinding.viewModel = detailViewModel
            }
        }
    }
}