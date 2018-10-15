package com.example.gowtham.chatlayoutapp.view

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gowtham.chatlayoutapp.R
import kotlinx.android.synthetic.main.attach_item.view.*

class AttachRecyclerViewAdapter : PagedListAdapter<String, AttachRecyclerViewAdapter.AttachRecyclerViewViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): AttachRecyclerViewViewHolder {
        val view=LayoutInflater.from(viewGroup.context).inflate(R.layout.attach_item,viewGroup,false)
        return AttachRecyclerViewViewHolder(view)
    }


    override fun onBindViewHolder(attachRecyclerViewViewHolder: AttachRecyclerViewViewHolder, position: Int) {
        val view=attachRecyclerViewViewHolder.itemView
        Glide.with(view.context)
                .load(getItem(position))
                .apply(RequestOptions()
                        .centerCrop()
                        .override(460, 900))
                .into(view.attach_item_image)
    }



    class AttachRecyclerViewViewHolder(view:View):RecyclerView.ViewHolder(view)

    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldConcert: String,
                                         newConcert: String): Boolean =
                    oldConcert == newConcert

            override fun areContentsTheSame(oldConcert: String,
                                            newConcert: String): Boolean =
                    oldConcert == newConcert
        }
    }
}