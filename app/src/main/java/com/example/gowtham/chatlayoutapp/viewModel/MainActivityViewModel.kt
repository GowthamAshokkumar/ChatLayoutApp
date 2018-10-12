package com.example.gowtham.chatlayoutapp.viewModel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import com.example.gowtham.chatlayoutapp.model.MyMessage
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivityViewModel(val db: AppDatabase) :ViewModel(){

    val messagesLiveData:MutableLiveData<List<MyMessage>> = MutableLiveData()
    val pagedListLiveData:MutableLiveData<PagedList<MyMessage>> = MutableLiveData()
    private val compositeDisposable=CompositeDisposable()

    init {
        val d=db.messageDao().getAll().subscribe {
            messagesLiveData.postValue(it)
        }

        val concertList: Flowable<PagedList<MyMessage>> =
                RxPagedListBuilder(db.messageDao().getAllMessages(), /* page size */ 50)
                        .buildFlowable(BackpressureStrategy.LATEST)

        val d1=concertList.subscribe {
            pagedListLiveData.postValue(it)
        }
        compositeDisposable.addAll(d,d1)
    }

    fun insertMessage(message:String,isSender:Boolean){
        val d=Completable.fromAction {
            db.messageDao().insert(MyMessage(null, message, isSender))
        }.subscribeOn(Schedulers.io())
                .subscribe()
        compositeDisposable.add(d)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}