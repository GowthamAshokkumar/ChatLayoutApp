package com.example.gowtham.chatlayoutapp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class MyMessage(@PrimaryKey(autoGenerate = true) var id:Int?, @ColumnInfo(name="message") var message:String, @ColumnInfo(name = "isSender") var isSender:Boolean)