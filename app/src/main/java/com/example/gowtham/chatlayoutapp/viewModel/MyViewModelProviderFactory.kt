package com.example.gowtham.chatlayoutapp.viewModel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.gowtham.chatlayoutapp.db.AppDatabase

class MyViewModelProviderFactory(private val application: Application,private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(application,appDatabase) as T
    }
}