package com.example.gowtham.chatlayoutapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.gowtham.chatlayoutapp.model.MyMessage

@Database(entities = [MyMessage::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}