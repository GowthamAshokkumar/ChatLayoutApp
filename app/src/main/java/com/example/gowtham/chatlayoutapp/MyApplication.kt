package com.example.gowtham.chatlayoutapp

import com.example.gowtham.chatlayoutapp.di.AppComponent
import com.example.gowtham.chatlayoutapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MyApplication :DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
    }
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent
}