package com.gunu.mylib.util

import androidx.fragment.app.Fragment
import com.gunu.mylib.ui.MainApplication
import com.gunu.mylib.ui.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MainApplication).repository
    return ViewModelFactory(repository)
}