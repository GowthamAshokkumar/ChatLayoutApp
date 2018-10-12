package com.example.gowtham.chatlayoutapp.di

import android.app.Application
import com.example.gowtham.chatlayoutapp.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,MainActivityModule::class,DatabaseModule::class])
interface AppComponent : AndroidInjector<MyApplication>{

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}