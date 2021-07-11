package com.gunu.mylib.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.gunu.mylib.R
import com.gunu.mylib.databinding.ActivityDetailBookBinding
import com.gunu.mylib.ui.EventObserver
import com.gunu.mylib.util.getViewModelFactory
import kotlinx.coroutines.*

class DetailBookActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by viewModels<DetailViewModel> { getViewModelFactory() }

    private val args: DetailBookActivityArgs by navArgs()

    private lateinit var viewBinding: ActivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.start(args.bookIsbn)

            withContext(Dispatchers.Main) {
                viewBinding = DataBindingUtil.setContentView(
                    this@DetailBookActivity,
                    R.layout.activity_detail_book
                )

                viewBinding.viewModel = detailViewModel

                setupToast()
                setupOpenUrlAction()
            }
        }
    }

    private fun setupToast() {
        detailViewModel.toastText.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupOpenUrlAction() {
        detailViewModel.openUrlEvent.observe(this, EventObserver {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))

            startActivity(intent)
        })
    }
}