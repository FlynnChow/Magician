package com.flynnchow.zero.component.multi_recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class IMultiHolder<Binding:ViewBinding>(val mBinding:Binding):RecyclerView.ViewHolder(mBinding.root) {
    abstract fun bindData(data: IMultiData<*>, position:Int)
}