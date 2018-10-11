package com.example.gowtham.chatlayoutapp.di

import com.example.gowtham.chatlayoutapp.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}