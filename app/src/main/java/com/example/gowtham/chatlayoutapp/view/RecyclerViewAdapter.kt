package com.example.gowtham.chatlayoutapp.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.model.MyMessage
import kotlinx.android.synthetic.main.item_message_left.view.*
import kotlinx.android.synthetic.main.item_message_right.view.*

class RecyclerViewAdapter:RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

    companion object {
       const val LEFT_TYPE=0
       const val RIGHT_TYPE=1
    }
    private var messages:List<MyMessage> = mutableListOf()
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerViewViewHolder {
        val view:View= if(type == LEFT_TYPE) {
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_message_left,viewGroup,false)
        }else{
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_message_right,viewGroup,false)
        }
        return RecyclerViewViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].isSender){
            LEFT_TYPE
        }else {
            RIGHT_TYPE
        }

    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(viewHolder: RecyclerViewViewHolder, i: Int) {
        if(messages[i].isSender) {
            viewHolder.view.message_text_left.text=messages[i].message
        }else{
            viewHolder.view.message_text_right.text=messages[i].message
        }
    }

    fun setMessages(messages:List<MyMessage>){
        this.messages=messages
        notifyDataSetChanged()
    }

    class RecyclerViewViewHolder(val view: View): RecyclerView.ViewHolder(view)
}