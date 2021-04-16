package com.flynnchow.zero.component.multi_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class DataBindAdapter<Binding: ViewBinding,ViewData:Any>(@LayoutRes private val id:Int):RecyclerView.Adapter<DataBindingHolder<Binding>>() {
    val data = ArrayList<ViewData>()

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingHolder<Binding> {
        val mBinding:Binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),id,parent,false)
        return DataBindingHolder(mBinding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: DataBindingHolder<Binding>, position: Int) {
        onBind(holder.mBinding,data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getData(index:Int):ViewData{
        return data[index]
    }

    abstract fun onBind(binding: Binding,viewData:ViewData,position:Int)
}