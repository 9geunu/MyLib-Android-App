package com.gunu.mylib.ui

import android.app.Application
import com.gunu.mylib.data.ServiceLocator

class MainApplication: Application() {

    val repository by lazy { ServiceLocator.provideRepository(this) }
}