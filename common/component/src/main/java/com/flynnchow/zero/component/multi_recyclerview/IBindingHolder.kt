package com.flynnchow.zero.component.multi_recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class IBindingHolder<Binding:ViewBinding,DATA>(val mBinding:Binding):RecyclerView.ViewHolder(mBinding.root) {
    abstract fun bindData(data: DATA?)
}