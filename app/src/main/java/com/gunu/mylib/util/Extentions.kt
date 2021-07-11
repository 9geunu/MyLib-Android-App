package com.gunu.mylib.util

import android.app.Activity
import androidx.fragment.app.Fragment
import com.gunu.mylib.ui.MainApplication
import com.gunu.mylib.ui.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MainApplication).repository
    return ViewModelFactory(repository)
}

fun Activity.getViewModelFactory(): ViewModelFactory {
    val repository = (this.applicationContext as MainApplication).repository
    return ViewModelFactory(repository)
}