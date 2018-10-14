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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.app.Application
import android.database.Cursor
import android.net.Uri
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivityViewModel(val application: Application,val db: AppDatabase) :ViewModel(){

    val pagedListLiveData:MutableLiveData<PagedList<MyMessage>> = MutableLiveData()
    val attachImagesLiveData:MutableLiveData<ArrayList<String>> = MutableLiveData()
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
        val d= Observable
                .fromArray(getAllShownImagesPath(application))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                   attachImagesLiveData.value=it
                }
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    /**
     * Getting All Images Path.
     *
     * @param activity
     * the activity
     * @return ArrayList with images Path
     */
    private fun getAllShownImagesPath(application: Application): ArrayList<String> {
        val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor
        val column_index_data: Int
        //val column_index_folder_name: Int
        val listOfAllImages = arrayListOf<String>()
        var absolutePathOfImage: String? = null

        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = application.contentResolver.query(uri, projection, null, null, null)

        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
        //column_index_folder_name = cursor
        //        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)

            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

}