package com.flynnchow.zero.component.multi_recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class SimpleHolder<Binding:ViewBinding>(val mBinding:Binding): RecyclerView.ViewHolder(mBinding.root)