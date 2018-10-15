package com.example.gowtham.chatlayoutapp.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import com.example.gowtham.chatlayoutapp.viewModel.MainActivityViewModel
import com.example.gowtham.chatlayoutapp.viewModel.MyViewModelProviderFactory
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var attachRecyclerViewAdapter: AttachRecyclerViewAdapter
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
        attachRecyclerViewAdapter=AttachRecyclerViewAdapter()
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


        mainActivityViewModel = ViewModelProviders.of(this,MyViewModelProviderFactory(application,appDatabase)).get(MainActivityViewModel::class.java)

        mainActivityViewModel.pagedListLiveData.observe(this, Observer {
            recyclerViewAdapter.submitList(it)
            messageEditText.setText("")
            messageRecyclerView.scrollToPosition(recyclerViewAdapter.currentList?.size!! -1 )
        })



        imageRecyclerView.visibility=View.GONE
        val layoutManager = GridLayoutManager(applicationContext, 3)
        imageRecyclerView.layoutManager = layoutManager
        imageRecyclerView.adapter = attachRecyclerViewAdapter
        imageRecyclerView.addItemDecoration(ItemOffsetDecoration(1))

        val d3=RxView.clicks(attachImageButton).subscribe{
            if(imageRecyclerView.visibility == View.GONE){
                RxPermissions(this@MainActivity)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe {status->
                            if(status){
                                imageRecyclerView.visibility=View.VISIBLE
                                mainActivityViewModel.getAttachImages()
                                mainActivityViewModel.attachImagesLiveData.observe(this, Observer { list ->
                                    attachRecyclerViewAdapter.submitList(list)
                                })
                            }else{
                                Toast.makeText(applicationContext,"permission denied",Toast.LENGTH_SHORT).show()
                            }
                        }
            }else{
                imageRecyclerView.visibility=View.GONE
            }
        }

        compositeDisposable.addAll(d1,d2,d3)

    }

}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                       state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}

