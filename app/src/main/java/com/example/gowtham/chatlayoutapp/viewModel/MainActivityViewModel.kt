package com.example.gowtham.chatlayoutapp.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import com.example.gowtham.chatlayoutapp.model.MyMessage
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    val messagesLiveData:MutableLiveData<List<MyMessage>> = MutableLiveData()
    private val compositeDisposable=CompositeDisposable()
    private val db = Room.databaseBuilder(application,
            AppDatabase::class.java, "message_database").build()


    init {
        val d=db.messageDao().getAll().subscribe {
            //Log.d("qwerty",it.toString())
            messagesLiveData.postValue(it)
        }
        compositeDisposable.add(d)
    }

    fun insertMessage(message:String,isSender:Boolean){
        db.messageDao().insert(MyMessage(null,message,isSender))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}