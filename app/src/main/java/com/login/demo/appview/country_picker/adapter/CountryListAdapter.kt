package com.login.demo.appview.country_picker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.login.demo.R
import com.login.demo.appview.country_picker.model.CountryItem
import com.login.demo.databinding.CountryItemBinding

class CountryListAdapter constructor(private val itemTouchListener: ItemTouchListener) :
    RecyclerView.Adapter<CountryListAdapter.CountryListViewHolder>() {

    private val list: ArrayList<CountryItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListViewHolder {
        val mItemBinding: CountryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.country_item,
            parent,
            false
        )
        return CountryListViewHolder(mItemBinding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) {
        val item = list[position]
        val itemClickListener: View.OnClickListener = View.OnClickListener {
            itemTouchListener.onItemClick(position, item)
        }
        holder.setItem(item)
        holder.setClickListener(itemClickListener)
    }

    fun setList(newList: ArrayList<CountryItem>) {
        val diffResult = DiffUtil.calculateDiff(CountryDiffUtil(newList, list))
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(newList)
    }

    fun updateList(updatedList: ArrayList<CountryItem>) {
        val newList = ArrayList<CountryItem>(list)
        newList.addAll(updatedList)
        setList(newList)
    }

    fun getItem(position: Int) = list[position]

    fun getList() = list

    interface ItemTouchListener {
        fun onItemClick(position: Int, item: CountryItem)
    }

    class CountryListViewHolder(val mItemBinding: CountryItemBinding) :
        RecyclerView.ViewHolder(mItemBinding.root) {

        fun setItem(item: CountryItem) {
            mItemBinding.item = item
        }

        fun setClickListener(clickListener: View.OnClickListener) {
            mItemBinding.root.setOnClickListener(clickListener)
        }
    }

    class CountryDiffUtil constructor(
        private val newList: ArrayList<CountryItem>? = null,
        private val oldList: ArrayList<CountryItem>? = null
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList?.get(oldItemPosition) == newList?.get(newItemPosition)
        }

        override fun getOldListSize(): Int {
            return oldList?.size ?: 0
        }

        override fun getNewListSize(): Int {
            return newList?.size ?: 0
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldList = oldList!![oldItemPosition]
            val newList = newList!![newItemPosition]
            return oldList === newList
        }
    }
}