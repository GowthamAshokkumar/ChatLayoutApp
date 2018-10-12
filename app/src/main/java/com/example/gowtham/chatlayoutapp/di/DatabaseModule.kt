package com.example.gowtham.chatlayoutapp.di

import android.app.Application
import android.arch.persistence.room.Room
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
 class DatabaseModule {
    @Provides
    @Named("message_database")
    fun provideDatabase(application:Application):AppDatabase = Room.databaseBuilder(application,
            AppDatabase::class.java, "message_database").build()


}