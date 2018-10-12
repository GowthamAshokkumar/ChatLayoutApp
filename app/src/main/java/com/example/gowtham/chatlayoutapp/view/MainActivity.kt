package com.example.gowtham.chatlayoutapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import com.example.gowtham.chatlayoutapp.viewModel.MainActivityViewModel
import com.example.gowtham.chatlayoutapp.viewModel.MyViewModelProviderFactory
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val compositeDisposable = CompositeDisposable()

    @field:[Inject Named("message_database")]
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)
        setUpViews()


    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


    private fun setUpViews() {
        recyclerViewAdapter = RecyclerViewAdapter()
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        messageRecyclerView.layoutManager = linearLayoutManager
        //linearLayoutManager.stackFromEnd=true
        messageRecyclerView.adapter = recyclerViewAdapter

        messageRecyclerView.addOnLayoutChangeListener { _: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldRight: Int, oldTop: Int, oldBottom: Int ->
            if (bottom < oldBottom) {
                linearLayoutManager.smoothScrollToPosition(messageRecyclerView, null, recyclerViewAdapter.itemCount)
            }
        }

        val d1 = RxView.clicks(sendImageButton)
                .observeOn(Schedulers.io())
                .subscribe {
                    mainActivityViewModel.insertMessage(messageEditText.text.toString(), true)
                }

        val d2 = RxView.clicks(recieveImageButton)
                .observeOn(Schedulers.io())
                .subscribe {
                    mainActivityViewModel.insertMessage(messageEditText.text.toString(), false)
                }
        compositeDisposable.addAll(d1, d2)

        mainActivityViewModel = ViewModelProviders.of(this,MyViewModelProviderFactory(appDatabase)).get(MainActivityViewModel::class.java)

        mainActivityViewModel.pagedListLiveData.observe(this, Observer {
            recyclerViewAdapter.submitList(it)
            messageEditText.setText("")
            messageRecyclerView.scrollToPosition(recyclerViewAdapter.currentList?.size!! -1 )
            Toast.makeText(applicationContext,"total items "+recyclerViewAdapter.itemCount,Toast.LENGTH_SHORT).show()
        })




    }

}

