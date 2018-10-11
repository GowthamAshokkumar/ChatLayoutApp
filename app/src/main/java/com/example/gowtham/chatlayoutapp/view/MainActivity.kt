package com.example.gowtham.chatlayoutapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import android.arch.persistence.room.Room
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.model.MyMessage
import com.example.gowtham.chatlayoutapp.viewModel.MainActivityViewModel
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel:MainActivityViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val compositeDisposable=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AndroidInjection.inject(this)


        recyclerViewAdapter= RecyclerViewAdapter()
        val linearLayoutManager=LinearLayoutManager(applicationContext)
        messageRecyclerView.layoutManager=linearLayoutManager
        messageRecyclerView.adapter=recyclerViewAdapter

        messageRecyclerView.addOnLayoutChangeListener{ _: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldRight: Int, oldTop: Int, oldBottom: Int ->
            if (bottom < oldBottom){
                linearLayoutManager.smoothScrollToPosition(messageRecyclerView, null, recyclerViewAdapter.itemCount)
            }
        }



        val d1=RxView.clicks(sendImageButton)
                .observeOn(Schedulers.io())
                .subscribe {
                mainActivityViewModel.insertMessage(messageEditText.text.toString(),true)
        }

        val d2=RxView.clicks(recieveImageButton)
                .observeOn(Schedulers.io())
                .subscribe{
                    mainActivityViewModel.insertMessage(messageEditText.text.toString(),false)
                }
        compositeDisposable.addAll(d1,d2)



        mainActivityViewModel=ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.messagesLiveData.observe(this, Observer {
            if (it != null) {
                recyclerViewAdapter.setMessages(it)
                messageEditText.setText("")
                messageRecyclerView.scrollToPosition(recyclerViewAdapter.itemCount -1)
            }
        })



    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
