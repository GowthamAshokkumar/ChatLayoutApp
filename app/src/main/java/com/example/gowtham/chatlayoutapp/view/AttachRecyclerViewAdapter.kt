package com.example.gowtham.chatlayoutapp.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gowtham.chatlayoutapp.R
import kotlinx.android.synthetic.main.attach_item.view.*

class AttachRecyclerViewAdapter : RecyclerView.Adapter<AttachRecyclerViewAdapter.AttachRecyclerViewViewHolder>(){
    var imagesStrings= arrayListOf<String>()
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): AttachRecyclerViewViewHolder {
        val view=LayoutInflater.from(viewGroup.context).inflate(R.layout.attach_item,viewGroup,false)
        return AttachRecyclerViewViewHolder(view)
    }

    override fun getItemCount(): Int = imagesStrings.size

    override fun onBindViewHolder(attachRecyclerViewViewHolder: AttachRecyclerViewViewHolder, position: Int) {
        val view=attachRecyclerViewViewHolder.itemView
        Glide.with(view.context)
                .load(imagesStrings[position])
                .apply(RequestOptions().centerCrop())
                .into(view.attach_item_image)
    }

    fun update(arrayList: ArrayList<String>){
        imagesStrings=arrayList
        notifyDataSetChanged()
    }

    class AttachRecyclerViewViewHolder(view:View):RecyclerView.ViewHolder(view)
}