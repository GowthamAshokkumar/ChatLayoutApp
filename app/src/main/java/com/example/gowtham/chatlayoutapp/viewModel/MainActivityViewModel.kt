package com.example.gowtham.chatlayoutapp.viewModel


import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.*
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import com.example.gowtham.chatlayoutapp.db.AppDatabase
import com.example.gowtham.chatlayoutapp.model.MyMessage
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivityViewModel(val application: Application,val db: AppDatabase) :ViewModel(){

    val pagedListLiveData:MutableLiveData<PagedList<MyMessage>> = MutableLiveData()
    lateinit var attachImagesLiveData: LiveData<PagedList<String>>
    private val compositeDisposable=CompositeDisposable()

    init {
        val concertList: Flowable<PagedList<MyMessage>> =
                RxPagedListBuilder(db.messageDao().getAllMessages(), /* page size */ 50)
                        .buildFlowable(BackpressureStrategy.LATEST)

        val d1=concertList.subscribe {
            pagedListLiveData.postValue(it)
        }
        compositeDisposable.add(d1)
    }

    fun insertMessage(message:String,isSender:Boolean){
        val d=Completable.fromAction {
            db.messageDao().insert(MyMessage(null, message, isSender))
        }.subscribeOn(Schedulers.io())
                .subscribe()
        compositeDisposable.add(d)

    }

    fun getAttachImages(){
        val config = PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(false)
                .build()
        attachImagesLiveData = LivePagedListBuilder<Int, String>(
                FilesDataSourceFactory(application), config).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}


class FilesDataSourceFactory(private val application: Application) :
        DataSource.Factory<Int, String>() {

    override fun create(): DataSource<Int, String> {
        return FilesDataSource(application)
    }
}


class FilesDataSource(val application: Application) : PositionalDataSource<String>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<String>) {
        callback.onResult(getFiles(params.loadSize, params.startPosition))
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<String>) {
        callback.onResult(getFiles(params.requestedLoadSize, params.requestedStartPosition), 0)
    }

    @SuppressLint("Recycle")
    private fun getFiles(limit: Int, offset: Int): MutableList<String> {
        val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        val cursor = application.contentResolver.query(uri, projection, null, null
                , "$orderBy DESC LIMIT $limit OFFSET $offset")!!
        val columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
        return cursor.mapToList {
            it.getString(columnIndex)
        }
    }

}

fun <T> Cursor.mapToList(callback: (Cursor) -> T): MutableList<T> {
    val list = mutableListOf<T>()
    moveToFirst()
    while (moveToNext()) {
        list.add(callback(this))
    }
    close()
    return list
}