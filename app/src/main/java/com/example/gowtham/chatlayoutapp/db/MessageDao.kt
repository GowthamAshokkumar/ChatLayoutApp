package com.example.gowtham.chatlayoutapp.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.example.gowtham.chatlayoutapp.model.MyMessage
import io.reactivex.Flowable

@Dao
interface MessageDao {

    @Query("select * from myMessage")
    fun getAll():Flowable<List<MyMessage>>

    @Insert(onConflict = REPLACE)
    fun insert(myMessage: MyMessage)

}