package com.example.gowtham.chatlayoutapp.view

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.gowtham.chatlayoutapp.model.MyMessage
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

class FullPageAdapter : PagedListAdapter<MyMessage, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val delegatesManager = AdapterDelegatesManager<List<MyMessage>>()
    private val delegatedPageList = DelegatedPageList()
    private val currentSafeList: List<MyMessage>
        get() = currentList!!

    init {
        delegatesManager.addDelegate(LeftAdapterDelegate())
        delegatesManager.addDelegate(RightAdapterDelegate())
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, type)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(delegatedPageList, position, holder, delegatedPageList)

    }

    override fun getItemViewType(position: Int): Int {

        return delegatesManager.getItemViewType(delegatedPageList, position)

    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyMessage>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: MyMessage,
                                         newConcert: MyMessage): Boolean =
                    oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: MyMessage,
                                            newConcert: MyMessage): Boolean =
                    oldConcert == newConcert
        }
    }


    inner class DelegatedPageList : List<MyMessage> {
        override val size: Int get() = currentSafeList.size
        override fun contains(element: MyMessage) = currentSafeList.contains(element)
        override fun containsAll(elements: Collection<MyMessage>) = currentSafeList.containsAll(elements)
        override fun get(index: Int): MyMessage = currentSafeList.get(index)
        override fun indexOf(element: MyMessage) = currentSafeList.indexOf(element)
        override fun isEmpty() = currentSafeList.isEmpty()
        override fun iterator() = currentSafeList.iterator()
        override fun lastIndexOf(element: MyMessage) = currentSafeList.lastIndexOf(element)
        override fun listIterator() = currentSafeList.listIterator()
        override fun listIterator(index: Int) = currentSafeList.listIterator(index)
        override fun subList(fromIndex: Int, toIndex: Int) = currentSafeList.subList(fromIndex, toIndex)
    }
}