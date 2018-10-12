package com.example.gowtham.chatlayoutapp.view

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.model.MyMessage
import kotlinx.android.synthetic.main.item_message_left.view.*
import kotlinx.android.synthetic.main.item_message_right.view.*

class RecyclerViewAdapter:PagedListAdapter<MyMessage,RecyclerViewAdapter.RecyclerViewViewHolder>(DIFF_CALLBACK) {

    companion object {
       const val LEFT_TYPE=0
       const val RIGHT_TYPE=1


        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<MyMessage>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert:MyMessage,
                                         newConcert: MyMessage): Boolean =
                    oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: MyMessage,
                                            newConcert: MyMessage): Boolean =
                    oldConcert == newConcert
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerViewViewHolder {
        val view:View= if(type == LEFT_TYPE) {
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_message_left,viewGroup,false)
        }else{
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_message_right,viewGroup,false)
        }
        return RecyclerViewViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {

        return if(getItem(position)!=null&&getItem(position)!!.isSender){
            LEFT_TYPE
        }else {
            RIGHT_TYPE
        }

    }


    override fun onBindViewHolder(viewHolder: RecyclerViewViewHolder, i: Int) {
        val myMessage=getItem(i)
        if(myMessage!=null){
           if(myMessage.isSender) {
                viewHolder.view.message_text_left.text=myMessage.message
           }else {
                viewHolder.view.message_text_right.text = myMessage.message
           }
        }
    }

//    fun setMessages(messages:List<MyMessage>){
//        this.messages=messages
//        notifyDataSetChanged()
//    }

    class RecyclerViewViewHolder(val view: View): RecyclerView.ViewHolder(view)

}