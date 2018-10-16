package com.example.gowtham.chatlayoutapp.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gowtham.chatlayoutapp.R
import com.example.gowtham.chatlayoutapp.model.MyMessage
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_message_left.view.*

class LeftAdapterDelegate : AdapterDelegate<List<MyMessage>>() {

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_message_left, parent, false)
        return RecyclerViewViewHolder(view)
    }

    override fun isForViewType(items: List<MyMessage>, position: Int): Boolean {
        return items[position].isSender
    }

    override fun onBindViewHolder(items: List<MyMessage>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val recyclerViewViewHolder = holder as RecyclerViewViewHolder
        recyclerViewViewHolder.itemView.message_text_left.text = items[position].message
    }

    class RecyclerViewViewHolder(view: View) : RecyclerView.ViewHolder(view)


}